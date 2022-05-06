package stage3.notebook.ControllersMVC;

import org.hibernate.query.Query;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import stage3.notebook.Record;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class NoteController{
    protected ClassPathXmlApplicationContext context;
    public NoteController(){
        context = new ClassPathXmlApplicationContext("springContext.xml");
    }

    @GetMapping("/get")
    public ModelAndView get(HttpServletRequest req, HttpServletResponse res){
        var webSession = req.getSession();
        var id = req.getParameter("recordId");
        var userId = (long)webSession.getAttribute("userId");

        //var session = SessionController.getInstance().getSession();
        var session = context.getBean(HiberSessionController.class).getSession();
        var trans = session.beginTransaction();

        //Находим запись по id или создаём новую запись

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

        //Если у этого пользователя нет доступа к данной записи, то выходим
        if(record.getUserId() != userId){
            return new ModelAndView("profile");
            //getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
        }

        var model = new ModelAndView("note");

        //Передаём данные найденной записи в jsp
        model.addObject("recordId", record.getId());
        model.addObject("content", record.getContent());
        model.addObject("marked", String.valueOf(record.getMarked()));
        model.addObject("remind", String.valueOf(record.getRemind()));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        if(record.getRemindTime() != null)
            model.addObject("remindTime", dateFormatter.format(record.getRemindTime()));
        if(record.getCreateTime() != null)
            model.addObject("creationTime", dateFormatter.format(record.getCreateTime()));

        return model;
    }

    @PostMapping("/save")
    public String save(HttpServletRequest req, HttpServletResponse res){
        var content = req.getParameter("content");
        var creationTimeStr = req.getParameter("creationTime");
        var marked = req.getParameter("marked") != null;
        var remind = req.getParameter("remind") != null;
        var remindDateStr = req.getParameter("remindTime");
        var recordId = Long.parseLong(req.getParameter("recordId"));

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

        //Создаёт запись, вставляем в неё параметры
        //Затем сохраняем её в БД

        Record record = new Record();
        record.setId(recordId);
        record.setContent(content);
        record.setCreateTime(creationTime);
        record.setMarked(marked);
        record.setRemind(remind);
        record.setRemindTime(remindDate);
        record.setUserId((Long)req.getSession().getAttribute("userId"));

        //var session = SessionController.getInstance().getSession();
        var session = context.getBean(HiberSessionController.class).getSession();
        var transaction = session.beginTransaction();
        session.merge(record);
        transaction.commit();

        //getServletContext().getRequestDispatcher("/profile.jsp").forward(request,response);
        return "profile";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest req, HttpServletResponse res){
        var id = req.getParameter("recordId");
        //var session = SessionController.getInstance().getSession();
        var session = context.getBean(HiberSessionController.class).getSession();
        var trans = session.beginTransaction();
        var record = session.get(Record.class, id);
        session.remove(record);
        trans.commit();

        //getServletContext().getRequestDispatcher("/profile.jsp").forward(req,resp);
        return "profile";
    }

    @GetMapping("/find")
    public ModelAndView find(HttpServletRequest req, HttpServletResponse res){
        //var session = SessionController.getInstance().getSession();
        var session = context.getBean(HiberSessionController.class).getSession();

        var leftDateStr = req.getParameter("leftDate");
        var rightDateStr = req.getParameter("rightDate");
        var marked = req.getParameter("marked") != null;
        var remind = req.getParameter("remind") != null;
        var contains = req.getParameter("contains");

        var findType = req.getParameter("findType");
        if(findType == null || findType.equals("")){
            return new ModelAndView("find-notebook");
        }

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

        var model = new ModelAndView("find-notebook");

        //Запихиваем найденные записи в атрибут
        model.addObject("records", records);

        //getServletContext().getRequestDispatcher("/find-notebook.jsp").forward(req, resp);
        return model;
    }
}
