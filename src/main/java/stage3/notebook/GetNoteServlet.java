package stage3.notebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class GetNoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webSession = req.getSession();
        var id = req.getParameter("recordId");
        var userId = (long)webSession.getAttribute("userId");

        var session = SessionController.getInstance().getSession();
        var trans = session.beginTransaction();

        Record record;
        if(id != null){
            record = session.get(Record.class, id);
        } else{
            record = new Record();
            record.setUserId(userId);
            record.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            session.persist(record);
        }

        trans.commit();

        if(record.getUserId() != userId){
            getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
        }

        req.setAttribute("recordId", record.getId());

        req.setAttribute("content", record.getContent());
        req.setAttribute("marked", String.valueOf(record.getMarked()));
        req.setAttribute("remind", String.valueOf(record.getRemind()));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        if(record.getRemindTime() != null)
            req.setAttribute("remindTime", dateFormatter.format(record.getRemindTime()));
        if(record.getCreateTime() != null)
            req.setAttribute("creationTime", dateFormatter.format(record.getCreateTime()));
        getServletContext().getRequestDispatcher("/note.jsp").forward(req,resp);
    }
}
