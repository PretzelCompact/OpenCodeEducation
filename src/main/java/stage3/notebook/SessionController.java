package stage3.notebook;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class SessionController {
    /*
    Класс, управляющий сессией связи с БД
    Использует singleton
     */

    private static SessionController instance;
    public static SessionController getInstance(){
        if(instance == null){
            instance = new SessionController();
        }
        return  instance;
    }

    private SessionController(){

    }

    private Session session;
    public Session getSession(){
        if(session == null || session != null && !session.isOpen()){
            Configuration conf = new Configuration();
            conf.configure();
            session = conf.buildSessionFactory().getCurrentSession();
        }
        return session;
    }
}
