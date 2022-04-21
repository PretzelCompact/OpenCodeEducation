package stage3.notebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class AuthorizationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter("username");
        var session = SessionController.getInstance().getSession();

        var trans = session.beginTransaction();
        var hql = "from  User u where u.name=:username";
        var query = session.createQuery(hql, User.class)
                .setParameter("username",username);
        var list = query.list();

        User user;
        if(list == null || list.size() == 0){
            user = new User();
            user.setName(username);
            session.persist(user);
        } else{
            user = list.get(0);
        }
        trans.commit();
        session.close();

        var webSession = req.getSession();
        webSession.setAttribute("userId", user.getId());
        webSession.setAttribute("userName", user.getName());

        session = SessionController.getInstance().getSession();
        var hqlRemind = "from Record r where r.userId=:userId and remind=true";
        var transRemind = session.beginTransaction();
        var queryRemind = session.createQuery(hqlRemind, Record.class)
                        .setParameter("userId", user.getId());

        var remindList = queryRemind.list();
        if(list != null && list.size() > 0){
            var sb = new StringBuilder();
            remindList.stream()
                    .forEach(r->{
                        var recordTime = r.getRemindTime().toLocalDateTime();
                        var nowTime = LocalDateTime.now();
                        var dur = Duration.between(recordTime,nowTime);
                        if(dur.toMinutes() <= 60)
                            sb.append(";" + r.getId());
                    });
            if(sb.length() > 0)
                req.setAttribute("records", sb.substring(1));
        }
        transRemind.commit();

        getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
    }
}
