package stage2.practice.two;

import java.util.Arrays;

public class LocalityQuery {
    /*
    Инкапсулирует запрос SQL в зависимости от параметров
     */

    private String distinct = "";

    private String localityNamePattern = "";
    private String populationDiapason = "";
    private String squareDiapason = "";
    private String dateDiapason = "";

    private String streetNamePattern = "";
    private String streetKind = "";

    private static String transformDiapason(String columnName, String diapason, String prefixAndPostfixOfValue){
        /*
        Парсит диапазон из строки в SQL
         */

        diapason = diapason.replace(" ","");

        var sb = new StringBuilder();

        //Извлечь значения, которые будут исключены(!=)
        String[] excluded = null;
        if(diapason.contains("exclude")){
            var parts = diapason.split("exclude");
            System.out.println(parts[0]);
            diapason = parts[0];
            excluded = parts[1].split(",");
        }
        if(excluded != null){
            for(var ex : excluded){
                sb.append(columnName + "<>" + prefixAndPostfixOfValue + ex + prefixAndPostfixOfValue);
            }
        }

        columnName = " and " + columnName;

        if(diapason.startsWith(">=") || diapason.startsWith("<=")){
            //case: >={значение}

            diapason = diapason.substring(0,2) + prefixAndPostfixOfValue + diapason.substring(2,diapason.length()) + prefixAndPostfixOfValue;
            sb.append(columnName + diapason);
        }else if(diapason.startsWith(">") || diapason.startsWith("<") || diapason.startsWith("=")){
            //case: >{значение}

            diapason = diapason.substring(0,1) + prefixAndPostfixOfValue + diapason.substring(1,diapason.length()) + prefixAndPostfixOfValue;
            sb.append(columnName + diapason);
        } else if(!diapason.equals("")){
            //case: ({значение};{значение}]

            var parts = diapason.split(";");
            String operatorOfLeftPart = "";
            String operatorOfRightPart = "";

            if(parts[0].startsWith("["))
                operatorOfLeftPart = ">=";
            else
                operatorOfLeftPart = ">";

            if(parts[1].endsWith("]"))
                operatorOfRightPart = "<=";
            else
                operatorOfRightPart = "<";

            sb.append(columnName + operatorOfLeftPart + prefixAndPostfixOfValue + parts[0].substring(1) + prefixAndPostfixOfValue);
            sb.append(columnName + operatorOfRightPart + prefixAndPostfixOfValue + parts[1].substring(0,parts[1].length()-1) + prefixAndPostfixOfValue);
        }
        return sb.toString();
    }

    public void setDistinct(boolean isDistinct){
        /*
        Установить запрос как distinct
         */
        if(!isDistinct)
            distinct = "";
        else
            distinct = " distinct";
    }

    public void restrictLocalityName(String namePattern){
        localityNamePattern = " and l.name like '" + namePattern + "'";
    }
    public void restrictPopulationDiapason(String diapason){
        populationDiapason = transformDiapason("population", diapason, "");
    }
    public void restrictSquareDiapason(String diapason){
        squareDiapason = transformDiapason("square", diapason, "");
    }
    public void restrictDate(String dateDiapason){
        dateDiapason = transformDiapason("establish_date", dateDiapason, "'");
    }
    public void restrictStreetName(String namePattern){
        var sb = new StringBuilder("0=0");
        Arrays.stream(namePattern.replace(" ", "").split(","))
                .forEach((p)->sb.append(" or s.name like '" + p + "'"));
        streetNamePattern = " and (" + sb.toString() + ")";
    }
    public void restrictStreetType(String streetType){
        this.streetKind = " and s.kind = '" + streetType + "'";
    }

    @Override
    public String toString(){
        /*
        Конвертировать сформированный запрос в SQL
         */

        String where = " where 1=1" + localityNamePattern + populationDiapason + squareDiapason + dateDiapason;
        String select = "select" + distinct + " l.id, l.name, l.kind, l.population, l.square, l.establish_date from Localities l";
        String join = "";
        if(!streetNamePattern.equals("") || !streetKind.equals("")){
            join = " inner join Streets s on l.id=s.localityId" + streetNamePattern + streetKind;
        }
        return select + join + where;
    }
}
