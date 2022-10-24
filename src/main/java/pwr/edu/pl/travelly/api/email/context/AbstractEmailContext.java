package pwr.edu.pl.travelly.api.email.context;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractEmailContext {
    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;

    public AbstractEmailContext() {
        this.context = new HashMap<>();
    }

    public <T> void init(T context){
        // configuration setup here
    }

    public void put(String key, Object value) {
        if (key != null) {
            this.context.put(key.intern(), value);
        }
    }
}
