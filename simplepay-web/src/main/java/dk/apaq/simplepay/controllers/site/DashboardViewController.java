package dk.apaq.simplepay.controllers.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.BaseController;

import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.StatisticEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class DashboardViewController extends BaseController {

    @Autowired
    private IPayService service;

    @RequestMapping(value = "/manage/dashboard", method = RequestMethod.GET)
    @Secured("ROLE_MERCHANT")
    public ModelAndView listEvents(@RequestParam(required = false) String type, @RequestParam(required = false) String entityId,  
           @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);

        Random random = new Random();
        Date now = new Date();
        int hour = (int) (now.getTime() / 3600000);
        List<StatisticEntry> entries = new ArrayList<StatisticEntry>();
        for(int i = hour;i<hour+24;i++) {
            entries.add(new StatisticEntry(m.getId(), hour, random.nextInt(99999), random.nextInt(9), random.nextInt(99), random.nextInt(9), random.nextInt(9), random.nextInt(9), random.nextInt(99), random.nextInt(9), random.nextInt(9)));
        }
        
        Map model = new HashMap();
        model.put("startHour", hour);
        model.put("endHour", hour+24 );
        model.put("statistics", entries);
        return new ModelAndView("dashboard", model);

    }
}
