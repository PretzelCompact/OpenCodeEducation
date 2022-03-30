package stage2.practice.three;

import stage2.practice.three.DatabaseRecords.CellRecord;
import stage2.practice.three.DatabaseRecords.GridRecord;
import stage2.practice.three.pathfinding.Cell;
import stage2.practice.three.pathfinding.Grid;
import stage2.practice.three.pathfinding.ObstacleCreators;
import stage2.practice.three.pathfinding.Pathfinders;
import stage2.practice.two.util.DatabaseConnector;
import stage2.practice.two.util.DatabaseExtractor;
import utils.Playable;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RunnerPart3 implements Playable {

    private static final int OBSTACLE_MAX_SIZE = 4;

    @Override
    public void play(BufferedReader br) {
        String pathToDB;
        String username;
        String password;

        while(true){
            try{
                System.out.println("Введите путь к файлу БД:");
                pathToDB = br.readLine();
                System.out.println("Введите имя пользователя:");
                username = br.readLine();
                System.out.println("Введите пароль:");
                password = br.readLine();
                break;
            } catch (IOException e){
                System.out.println("Ошибка ввода. Попробуйте снова");
            }
        }

        var connector = new DatabaseConnector(pathToDB);
        try(var connection = connector.getConnection(username, password)){
            connection.setAutoCommit(false);
            createTables(connection);
            mainMenu(br, connection);
        } catch (SQLException e){
            System.out.println("Ошибка связи с БД");
        }
    }

    private void createTables(Connection connection){
        try{
            var statement = connection.createStatement();
            statement.execute("create table if not exists Grids(" +
                    "id identity not null," +
                    "name varchar(255), " +
                    "width int,"+
                    "height int,"+
                    "state varchar(255), " +
                    "primary key(id))");

            statement.execute("create table if not exists Cells(" +
                    "id identity not null," +
                    "x int,"+
                    "y int,"+
                    "state varchar(255), " +
                    "gridId bigint, " +
                    "primary key(id)," +
                    "foreign key(gridId) references Grids(id) "+
                    "on update Cascade " +
                    "on delete set null)");
            statement.close();
            connection.commit();
        } catch (SQLException e){
            System.out.println("Ошибка создания табилц в БД");
        }
    }

    private void mainMenu(BufferedReader br, Connection connection){
        while(true){
            System.out.println("--------------------");
            printMainHelp();

            try{
                String[] input;
                while(true){
                    input = br.readLine().split(" ");
                    switch (input[0]){
                        case "list":
                            listAllGrids(connection);
                            break;
                        case "choose":
                            gridMenu(br,connection,input[1]);
                            break;
                        case "create":
                            createMenu(br,connection);
                            break;
                        case "exit":
                            return;
                        default:
                            System.out.println("Неизвестная команда");
                            break;
                    }
                }
            } catch (IOException e){
                System.out.println("Ошибка ввода");
            }
        }
    }

    private void printMainHelp(){
        System.out.println("list -- отобразить все сетки");
        System.out.println("choose [имя сетки] -- выбрать указанную сетку");
        System.out.println("create -- создать новую сетку");
        System.out.println("exit -- выход");
    }
    private void printGridHelp(){
        System.out.println("params -- показать параметры сетки");
        System.out.println("delete -- удалить сетку");
        System.out.println("print -- вывести сетку в консоль");
        System.out.println("init -- инициализировать сетку");
        System.out.println("restore -- очистить сетку от путей");
        System.out.println("astar -- выполнить поиск пути с помощью a*");
        System.out.println("bfs -- выполнить поиск пути с помощью breadths-first search");
        System.out.println("dfs -- выполнить поиск пути с помощью deep-first search");
        System.out.println("exit -- вернуться в главное меню");
    }

    private void listAllGrids(Connection connection){
        try{
            var sql = "select * from Grids";
            var statement = connection.createStatement();
            statement.execute(sql);
            var list = DatabaseExtractor.extractList(new GridRecord(),statement.getResultSet());
            if(list.size() > 0){
                for(var g : list)
                    System.out.println(g.getName());
            } else{
                System.out.println("Сетки в БД отсутствуют");
            }
            statement.close();
        } catch (SQLException e){
            System.out.println("Ошибка обращение к БД");
        }
    }

    private void createMenu(BufferedReader br, Connection connection){
        var gridRecord = new GridRecord();
        gridRecord.setState(GridRecord.State.NotInitialized);
        try{
            System.out.println("Введите имя сетки:");
            gridRecord.setName(br.readLine());
            System.out.println("Введите ширину сетки:");
            gridRecord.setWidth(Integer.parseInt(br.readLine()));
            System.out.println("Введите высоту сетки:");
            gridRecord.setHeight(Integer.parseInt(br.readLine()));

        } catch (IOException e){
            System.out.println("Ошибка ввода");
            return;
        }
        try{
            var sql = "insert into Grids (name,width,height,state) values(?,?,?,?)";
            var statement = connection.prepareStatement(sql);
            gridRecord.setValuesToStatement(statement);
            statement.execute();
            connection.commit();
            statement.close();
            System.out.println("Сетка успешно создана");
        } catch(SQLException e){
            System.out.println("Ошибка добавления записи в таблицу Grids БД");
        }
    }

    private void gridMenu(BufferedReader br, Connection connection, String name){
        var gridRec = new GridRecord();
        try{
            var sql = "select * from Grids where name=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            statement.execute();
            gridRec = DatabaseExtractor.extractOne(gridRec, statement.getResultSet());
            statement.close();

        } catch (SQLException e){
            System.out.println("Ошибка получения сетки из БД");
            return;
        }

        Grid grid = null;
        if(gridRec.getState() != GridRecord.State.NotInitialized)
            grid = getGrid(gridRec,connection);

        String input;
        while(true){
            System.out.println("--------------------");
            try{
                printGridHelp();
                input = br.readLine();
                switch (input){
                    case "params":
                        System.out.println(gridRec.toString());
                        break;
                    case "delete":
                        if(deleteGridFromDB(gridRec,connection)){
                            System.out.println("Сетка успешно удалена");
                            return;
                        } else{
                            System.out.println("Ошибка удаление сетки из БД");
                            break;
                        }
                    case "restore":
                        if(grid != null){
                            grid.restore();
                            updateGridInDB(gridRec,grid,connection);
                            System.out.println("Сетка восстановлена");
                        } else{
                            System.out.println("Сначала инициализируйте сетки");
                        }
                        break;
                    case "print":
                        if(grid == null){
                            System.out.println("Сначала инициализируйте сетку");
                        } else{
                            System.out.println(grid.toString());
                        }
                        break;
                    case "init":
                        if(grid == null){
                            grid = initGrid(gridRec,connection);
                        } else{
                            System.out.println("Сетка уже была инициализирована. Операция отклонена");
                        }
                        break;
                    case "astar":
                        if(grid == null){
                            System.out.println("Сначала инициализируйте сетку");
                        } else if(gridRec.getState() != GridRecord.State.Initialized){
                            System.out.println("Сначала очистите сетку с помощью restore");
                        } else{
                            Pathfinders.AStar(grid,grid.getStartCell(),grid.getFinishCell());
                            gridRec.setState(GridRecord.State.PathFromAStartCreated);
                            updateGridInDB(gridRec,grid,connection);
                        }
                        break;
                    case "dfs":
                        if(grid == null){
                            System.out.println("Сначала инициализируйте сетку");
                        } else if(gridRec.getState() != GridRecord.State.Initialized){
                            System.out.println("Сначала очистите сетку с помощью restore");
                        } else{
                            Pathfinders.DFS(grid,grid.getStartCell(),grid.getFinishCell());
                            gridRec.setState(GridRecord.State.PathFromDFSCreated);
                            updateGridInDB(gridRec,grid,connection);
                        }
                        break;
                    case "bfs":
                        if(grid == null){
                            System.out.println("Сначала инициализируйте сетку");
                        } else if(gridRec.getState() != GridRecord.State.Initialized){
                            System.out.println("Сначала очистите сетку с помощью restore");
                        } else{
                            Pathfinders.BFS(grid,grid.getStartCell(),grid.getFinishCell());
                            gridRec.setState(GridRecord.State.PathFromBFSCreated);
                            updateGridInDB(gridRec,grid,connection);
                        }
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Неизвестная команда");
                        break;
                }
            } catch (IOException e){
                System.out.println("Ошибка ввода");
            }
        }
    }
    private Grid getGrid(GridRecord gridRecord, Connection connection){
        List<CellRecord> cellRecords;

        try{
            var sql = "select * from Cells where gridId=?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, gridRecord.getId());
            statement.execute();
            cellRecords = DatabaseExtractor.extractList(new CellRecord(),statement.getResultSet());
            statement.close();
        } catch (SQLException e){
            System.out.println("Ошибка чтения клеток из БД");
            return null;
        }

        var grid = new Grid(gridRecord.getWidth(), gridRecord.getHeight());
        for(var rec : cellRecords){
            int x = rec.getX();
            int y = rec.getY();
            var s = rec.getState();
            grid.getCell(x,y).setState(s);
        }
        return grid;
    }

    private boolean deleteGridFromDB(GridRecord rec, Connection connection){
        try{
            var sql = "delete from Grids where id=" + rec.getId();
            var statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.commit();
            return true;
        } catch (SQLException e){
            return false;
        }
    }
    private void updateGridInDB(GridRecord rec, Grid grid, Connection connection){
        try{
            var sql = "update Grids set name=?, width=?, height=?, state=? where id=?";
            var statementGrid = connection.prepareStatement(sql);
            rec.setValuesToStatement(statementGrid);
            statementGrid.setLong(5,rec.getId());
            statementGrid.execute();
            statementGrid.close();

            var sqlCells = "select * from Cells where gridId=" + rec.getId();
            var statementCells = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            statementCells.execute(sqlCells);
            var set = statementCells.getResultSet();

            while(set.next()){
                int x = set.getInt("x");
                int y = set.getInt("y");
                var cell = grid.getCell(x,y);
                putCellValuesIntoResultSet(cell,rec.getId(),set);
                set.updateRow();
            }

            statementCells.close();
            connection.commit();
        } catch (SQLException e){
            System.out.println("Ошибка обновления сетки в БД");
            e.printStackTrace();
        }
    }

    private void putCellValuesIntoResultSet(Cell cell, long gridId, ResultSet set) throws SQLException{
        set.updateInt("x",cell.getX());
        set.updateInt("y",cell.getY());
        set.updateString("state", cell.getState().toString());
        set.updateLong("gridId",gridId);
    }

    private void addCellsToDB(Grid grid, long gridId, Connection connection){
        try{
            var sql = "insert into Cells (x,y,state,gridId) values(?,?,?,?)";
            var statement = connection.prepareStatement(sql);
            for(int i = 0; i < grid.getWidth(); i++)
                for (int j = 0; j < grid.getHeight(); j++){
                    var cellRec = new CellRecord();
                    cellRec.readFromCell(grid.getCell(i,j),-1,gridId);
                    cellRec.setValuesToStatement(statement);
                    statement.addBatch();
                }
            statement.executeBatch();
            statement.close();
            connection.commit();
        } catch (SQLException e){
            System.out.println("Ошибка добавления клетки в БД");
            e.printStackTrace();
        }
    }

    private Grid initGrid(GridRecord gridRecord, Connection connection){
        var grid = new Grid(gridRecord.getWidth(), gridRecord.getHeight());
        grid.initialize();
        ObstacleCreators.lineScan(grid,4);
        addCellsToDB(grid, gridRecord.getId(), connection);
        gridRecord.setState(GridRecord.State.Initialized);
        return grid;
    }
}
