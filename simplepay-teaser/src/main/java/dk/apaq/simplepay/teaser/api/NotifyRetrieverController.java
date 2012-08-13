package dk.apaq.simplepay.teaser.api;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.simplepay.teaser.ITeaserService;
import dk.apaq.simplepay.teaser.MessageBundle;
import dk.apaq.simplepay.teaser.model.NotificationReceiver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotifyRetrieverController {

    @Autowired
    private ITeaserService service;

    @RequestMapping(value = "/notificationreceiver", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addNotificationReceiver(HttpServletRequest request, @RequestParam String email) {
        String result;
        String message;

        MessageBundle mb = MessageBundle.get(request.getLocale(), "dk.apaq.simplepay.teaser.i18n.Messages");

        if (email == null || "".equals(email)) {
            result = "INVALID_MAIL";
            message = mb.get("invalidEmailAddress");
        } else {

            Filter filter = new LikeFilter("mail", email, false);
            List<String> ids = service.getNotificationRecievers().listIds(filter, null);

            if (!ids.isEmpty()) {
                result = "ALREADY_IN_LIST";
                message = mb.get("notifyReceiverAlreadyInList");
            } else {
                service.getNotificationRecievers().create(new NotificationReceiver(email, request.getLocale().toString(), request.getRemoteAddr()));
                result = "SUCCESS";
                message = mb.get("notifyReceiverAddedToList");
            }
        }

        Map<String, String> info = new HashMap<String, String>();
        info.put("result", result);
        info.put("message", message);
        return info;
    }
}
