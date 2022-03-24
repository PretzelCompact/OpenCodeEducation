package stage2.practice.two;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;
import stage2.practice.two.util.DatabaseConnector;
import utils.Playable;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class RunnerPart2 implements Playable {
    @Override
    public void play(BufferedReader br){
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

        try(Session session = new Session(connector, username, password)){
            System.out.println("Сессия открыта");
            String[] input;
            while(true){
                System.out.println("-------------------");
                printMainMenuHelp();
                System.out.println("-------------------");
                input = br.readLine().split(" ");
                switch (input[0]){
                    case "list":
                        listLocalities(br, session);
                        break;
                    case "create":
                        createLocalities(br, session);
                        break;
                    case "choose":
                        localityMenu(br, session, input[1]);
                        break;
                    case "find":
                        findMenu(br,session);
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Неизвестная команда");
                }
            }

        } catch (Exception e){
            System.out.println("Ошибка");
            e.printStackTrace();
        }
    }
    private void printMainMenuHelp(){
        System.out.println("list -- вывести список всех населённых пунктов");
        System.out.println("create -- войти в меню создания населённых пунктов");
        System.out.println("find -- войти в меню поиска населённых пунктов");
        System.out.println("choose [название пункта] -- войти в меню конкретного пункта");
        System.out.println("exit -- выход");
    }
    private void printLocalityMenuHelp(){
        System.out.println("list -- вывести список всех улиц данного пункта");
        System.out.println("list [паттерн] -- вывести список всех улиц, чьё имя соответствует паттерну SQL");
        System.out.println("update -- изменить информацию о данном пункте");
        System.out.println("delete -- удалить населённый пункт");
        System.out.println("addStreets -- войти в меню создания улиц для данного пункта");
        System.out.println("exit -- вернуться в основное меню");
    }
    private void printFindMenuHelp(){
        System.out.println("Перед началом поиска будет сформирован запрос из указанный критериев");
        System.out.println("Чтобы пропустить ввод критерия введите '-'(тире)");
        System.out.println("Дата указывается в формате YYYY-MM-DD без кавычек");
        System.out.println("Диапазон может иметь один из следующий видов ( '[' и ')' -- математические символы записи промежутков):");
        System.out.println("{>,<,=,>=<=}{значение}");
        System.out.println("ИЛИ:");
        System.out.println("[{значение};{значение}]");
        System.out.println("({значение};{значение}]");
        System.out.println("[{значение};{значение})");
        System.out.println("({значение};{значение})");
        System.out.println("После указания диапазона можно ввести список исключаемых(!=) значений этого диапазона:");
        System.out.println("{диапазон} exclude {значение1},{значение2},{значениеN}");
        System.out.println("При этом диапазон может быть пустым");
        System.out.println("Примерs:");
        System.out.println(">=10 exclude 12,14,14");
        System.out.println("(2010-1-1;2020-1-1) exclude 2013-1-1");
        System.out.println("exclude 1,2,3");
    }

    private void localityMenu(BufferedReader br, Session session, String name){
        Locality loc;
        try{
            loc = session.getLocalityFromName(name);
        } catch (SQLException e){
            System.out.println("Населённый пункт с именем " + name + " нот фаунд");
            return;
        }

        try{
            String[] input;
            while(true){
                printLocalityMenuHelp();
                input = br.readLine().split(" ");
                switch (input[0]){
                    case "list":
                        List<Street> streets;
                        if(input.length == 1){
                            streets = session.getStreetsOfLocality(loc.getId());
                        } else {
                            streets = session.getStreetsOfLocality(loc.getId(), input[1]);
                        }
                        if(streets == null){
                            System.out.println("Улицы отсутствуют");
                            break;
                        }
                        for(var s : streets)
                            System.out.println(s);
                        System.out.println("'yes'-удалить найденые улицы, 'no'-помиловать");
                        if(br.readLine().equals("yes")) {
                            var arr = streets.toArray(new Street[0]);
                            session.deleteStreets(arr);
                        }
                        break;
                    case "update":
                        var newLoc = createLocality(br);
                        newLoc.setId(loc.getId());
                        loc = newLoc;
                        session.updateLocalities(loc);
                        break;
                    case "delete":
                        session.deleteLocalities(loc);
                        return;
                    case "addStreets":
                        createStreets(br,session,loc.getId());
                        break;
                    case "exit":
                        return;
                }
            }
        } catch (IOException e){
            System.out.println("Ошибка ввода");
        } catch (SQLException e){
            System.out.println("Ошибка запроса");
            e.printStackTrace();
        }
    }

    private void findMenu(BufferedReader br, Session session){
        printFindMenuHelp();
        System.out.println("");

        var query = new LocalityQuery();
        query.setDistinct(true);
        String input;

        try {
            System.out.println("Введите SQL-паттерн имени населённого пункта:");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictLocalityName(input);

            System.out.println("Введите ограничение численности населённого пункта(int):");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictPopulationDiapason(input);

            System.out.println("Введите ограничение площади населённого пункта(float):");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictSquareDiapason(input);

            System.out.println("Введите ограничение даты основания населённого пункта(YYYY-MM-DD):");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictDate(input);

            System.out.println("Введите SQL-паттерны имени улиц, которые должен содержать населённый пункт через запятую:");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictStreetName(input);

            System.out.println("Введите тип улиц, который должен содержать населённый пункт:");
            input = br.readLine();
            if(!input.equals("-"))
                query.restrictStreetType(input);

            var locs = session.getLocalitiesFromQuery(query);
            var sb = new StringBuilder("\nЗапрос:\n"+ query.toString() +"\n\nРезультат запроса:");
            for(var l : locs){
                sb.append("\n" + l.toString());
            }

            System.out.println("Введите 'yes', если нужно сохранить результат запроса в файл или 'no'");
            input = br.readLine();
            if(input.equals("yes")){
                System.out.println("Введите путь к файлу:");
                var path = br.readLine();
                writeToFile(path,sb.toString());
            } else{
                System.out.println(sb.toString());
            }
        } catch (IOException e){
            System.out.println("Ошибка ввода");
        } catch (SQLException e){
            System.out.println("Ошибка запроса");
            System.out.println(query.toString());
        }
    }

    private void writeToFile(String path, String text){
        try(var fos = new FileOutputStream(path)){
            var bytes = text.getBytes();
            fos.write(bytes,0,bytes.length);
            System.out.println("Данные записаны в файл");
        } catch(IOException e){
            System.out.println("Ошибка записи в файл");
        }
    }

    private void createLocalities(BufferedReader br, Session session){
        String input;
        var locs = new ArrayList<Locality>();
        while(true){
            System.out.println("Введите continue, чтобы создать новый населённый пункт или stop");
            try{
                input = br.readLine();
                if(input.equals("continue"))
                    locs.add(createLocality(br));
                else if(input.equals("stop")){
                    var arr = locs.toArray(new Locality[0]);
                    session.createLocalities(arr);
                    System.out.println("Записи успешно добавлены");
                    break;
                }
                else{
                    System.out.println("Ошибка ввода");
                }
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                continue;
            } catch (SQLException e){
                System.out.println("Ошибка обращения к БД");
                e.printStackTrace();
            }

        }
    }

    private void createStreets(BufferedReader br, Session session, long localityId){
        String input;
        var streets = new ArrayList<Street>();
        while(true){
            System.out.println("Введите continue, чтобы создать новую улицу или stop");
            try{
                input = br.readLine();
                if(input.equals("continue"))
                    streets.add(createStreet(br, localityId));
                else if(input.equals("stop")){
                    var arr = streets.toArray(new Street[0]);
                    session.createStreets(arr);
                    break;
                }
                else{
                    System.out.println("Ошибка ввода");
                }
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                continue;
            } catch (SQLException e){
                System.out.println("Ошибка обращения к БД");
                e.printStackTrace();
            }

        }
    }

    private void listLocalities(BufferedReader br, Session session){
        List<Locality> locs;
        try{
            locs = session.getLocalitiesFromQuery(new LocalityQuery());
        } catch (SQLException e){
            System.out.println("Ошибка при обращении к БД");
            e.printStackTrace();
            return;
        }
        if(locs.size() != 0){
            var sb = new StringBuilder();
            for(var l : locs)
                sb.append(l.toString() + '\n');
            System.out.println(sb.substring(0,sb.length()-1));
        } else{
            System.out.println("В базе нет населённых пунктов");
        }
    }
    private Locality createLocality(BufferedReader br){
        try{
            var loc = new Locality();
            System.out.println("Введите название:");
            loc.setName(br.readLine());
            System.out.println("Введите тип:");
            loc.setKind(br.readLine());
            System.out.println("Введите площадь:");
            loc.setSquare(Float.parseFloat(br.readLine()));
            System.out.println("Введите дату основания(YYYY-MM-DD):");
            loc.setEstablishmentDate(Date.valueOf(br.readLine()));
            System.out.println("Введите количество жителей в тысячах:");
            loc.setPopulation(Integer.parseInt(br.readLine()));
            return loc;
        } catch (IOException e){
            System.out.println("Ошибка ввода");
            return null;
        }
    }
    private Street createStreet(BufferedReader br, long localityId){
        try{
            var street = new Street();
            System.out.println("Введите название:");
            street.setName(br.readLine());
            System.out.println("Введите тип:");
            street.setKind(br.readLine());
            street.setLocalityId(localityId);
            return street;
        } catch (IOException e){
            System.out.println("Ошибка ввода");
            return null;
        }
    }
}
