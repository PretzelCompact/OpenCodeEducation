package stage3.notebook.ControllersMVC;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HiberSessionController {
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
