package stage3.notebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SavenoteServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var content = request.getParameter("content");
        var creationTimeStr = request.getParameter("creationTime");
        var marked = request.getParameter("marked") != null;
        var remind = request.getParameter("remind") != null;
        var remindDateStr = request.getParameter("remindTime");
        var recordId = Long.parseLong(request.getParameter("recordId"));

        //Парсим время
        Timestamp creationTime = null;
        Timestamp remindDate = null;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        try{
            creationTime = new Timestamp(dateFormatter.parse(creationTimeStr).getTime());
        } catch (Exception e){  }
        try {
            remindDate = new Timestamp(dateFormatter.parse(remindDateStr).getTime());
        } catch(Exception e) {}

        Record record = new Record();
        record.setId(recordId);
        record.setContent(content);
        record.setCreateTime(creationTime);
        record.setMarked(marked);
        record.setRemind(remind);
        record.setRemindTime(remindDate);
        record.setUserId((Long)request.getSession().getAttribute("userId"));

        var session = SessionController.getInstance().getSession();
        var transaction = session.beginTransaction();
        session.merge(record);
        transaction.commit();

        //response.sendRedirect("/profile.jsp");
        getServletContext().getRequestDispatcher("/profile.jsp").forward(request,response);
    }
}
