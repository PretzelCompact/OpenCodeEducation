package stage3.notebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var id = req.getParameter("recordId");
        var session = SessionController.getInstance().getSession();
        var trans = session.beginTransaction();
        var record = session.get(Record.class, id);
        session.remove(record);
        trans.commit();

        getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
    }
}
