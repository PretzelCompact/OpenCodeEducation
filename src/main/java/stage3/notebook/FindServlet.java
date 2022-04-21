package stage3.notebook;

import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

public class FindServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = SessionController.getInstance().getSession();

        var leftDateStr = req.getParameter("leftDate");
        var rightDateStr = req.getParameter("rightDate");
        var marked = req.getParameter("marked") != null;
        var remind = req.getParameter("remind") != null;
        var contains = req.getParameter("contains");

        var findType = req.getParameter("findType");

        //Парсим дату из атрибута
        Timestamp leftDate = null;
        Timestamp rightDate = null;

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        try{
            leftDate = new Timestamp(dateFormatter.parse(leftDateStr).getTime());
        } catch (Exception e){
            leftDate = Timestamp.valueOf(LocalDateTime.MIN);
        }
        try{
            rightDate = new Timestamp(dateFormatter.parse(rightDateStr).getTime());
        } catch (Exception e){
            rightDate = Timestamp.valueOf(LocalDateTime.MAX);
        }

        //Строим запрос для записи и находим
        List<Record> records;
        var trans = session.beginTransaction();
        Query<Record> query;

        if(findType.equals("selected")) {
            //Сформировать запрос в соответствии с введёнными параметрами

            var hql = new StringBuilder("from Record r where (r.createTime between :leftDate and :rightDate)" +
                    " and r.marked = :marked and r.remind = :remind and r.userId=:userId");
            if(contains != null && !contains.equals(""))
                hql.append(" and r.content like :contains");

            query = session.createQuery(hql.toString(),Record.class)
                    .setParameter("leftDate", leftDate)
                    .setParameter("rightDate", rightDate)
                    .setParameter("marked", marked)
                    .setParameter("remind", remind)
                    .setParameter("userId", req.getSession().getAttribute("userId"));
        } else {
            //Выбрать ВСЕ записи пользователя

            query = session.createQuery("from Record r where r.userId=:userId", Record.class)
                    .setParameter("userId", req.getSession().getAttribute("userId"));
        }

        records = query.list();
        trans.commit();

        //Запихиваем id всех найденых записей в атрибут

        if (records != null && records.size() > 0) {
            var sb = new StringBuilder();
            records.stream().forEach(r -> {
                sb.append(";" + r.getId());
            });
            req.setAttribute("records", sb.substring(1));
        }
        getServletContext().getRequestDispatcher("/find-notebook.jsp").forward(req, resp);
    }
}
