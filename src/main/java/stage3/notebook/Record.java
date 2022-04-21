package stage3.notebook;

import java.sql.Timestamp;

public class Record {
    private long id;
    private long userId;
    private String content;
    private boolean marked;
    private boolean remind;
    private Timestamp createTime;
    private Timestamp remindTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean getRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Timestamp remindTime) {
        this.remindTime = remindTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
