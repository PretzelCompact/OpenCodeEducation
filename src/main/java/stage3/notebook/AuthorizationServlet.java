package stage3.notebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuthorizationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter("username");
        var session = SessionController.getInstance().getSession();

        //Находим пользователя по имени
        //Если не находится, то создаём нового

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

        //Находим все записки, о которых нужно напомнить
        //(За час до истечения времени)

        session = SessionController.getInstance().getSession();
        var hqlRemind = "from Record r where r.userId=:userId and remind=true";
        var transRemind = session.beginTransaction();
        var queryRemind = session.createQuery(hqlRemind, Record.class)
                        .setParameter("userId", user.getId());

        var remindList = queryRemind.list();
        var recordsToShow = new ArrayList<Record>();
        if(list != null && list.size() > 0){
            remindList.stream()
                    .forEach(r->{
                        if(r.getRemindTime() != null) {
                            var recordTime = r.getRemindTime().toLocalDateTime();
                            var nowTime = LocalDateTime.now();
                            var dur = Duration.between(recordTime, nowTime);
                            if (dur.toMinutes() <= 60)
                                recordsToShow.add(r);
                        }
                    });
            if(recordsToShow.size() > 0)
                req.setAttribute("records", recordsToShow);
        }
        transRemind.commit();

        getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
    }
}
