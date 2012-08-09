package dk.apaq.simplepay.teaser.api;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.simplepay.teaser.ITeaserService;
import dk.apaq.simplepay.teaser.model.NotificationReceiver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotifyRetrieverController {
    
    @Autowired
    private ITeaserService service;
    
    @RequestMapping(value="/notificationreceiver", method= RequestMethod.POST)
    @ResponseBody
    public String addNotificationReceiver(String mail) {
        if(mail==null || "".equals(mail)) {
            return "INVALID_MAIL";
        }
        
        Filter filter = new LikeFilter("mail", mail, false);
        List<String> ids = service.getNotificationRecievers().listIds(filter, null);
        
        if(!ids.isEmpty()) {
            return "ALREADY_IN_LIST";
        }
        
        service.getNotificationRecievers().create(new NotificationReceiver(mail));
        return "SUCCESS";
    }
}
