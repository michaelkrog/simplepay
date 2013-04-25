package dk.apaq.simplepay.controllers.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.BaseController;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.StatisticEntry;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author michael
 */
@Controller
public class StatisticApiController extends BaseController {

    private final IPayService service;
    private final RestErrorHandler errorHandler;

    @Autowired
    public StatisticApiController(IPayService service, RestErrorHandler errorHandler) {
        this.service = service;
        this.errorHandler = errorHandler;
    }
    
    @ExceptionHandler(Throwable.class)
    public void handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        errorHandler.handleThrowable(request, response, ex);
    }
    
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, headers = "Accept=application/json")
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Iterable<StatisticEntry> listStatistics(@RequestParam(required=false) Date start, @RequestParam(required=false) Date end) {
        Merchant m = ControllerUtil.getMerchant(service);
        
        if(start == null) {
            start = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        }
        
        if(end == null) {
            end = DateUtils.ceiling(new Date(), Calendar.DAY_OF_MONTH);
        }
        
        Random random = new Random();
        Date now = new Date();
        int hour = (int) (now.getTime() / 3600000);
        List<StatisticEntry> entries = new ArrayList<StatisticEntry>();
        for(int i = hour;i<hour+24;i++) {
            entries.add(new StatisticEntry(m.getId(), hour, random.nextInt(99999), random.nextInt(9), random.nextInt(99), random.nextInt(9), random.nextInt(9), random.nextInt(9), random.nextInt(99), random.nextInt(9), random.nextInt(9)));
        }
        
        
        return entries;
    }
}
