package dk.apaq.simplepay.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Limit;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.framework.criteria.simplequery.QueryException;
import dk.apaq.framework.criteria.simplequery.SimpleQueryParser;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller that specifies basic functionality extended by other controllers.
 */
public class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private IPayService service;
    private SimpleQueryParser queryParser = new SimpleQueryParser();

    public SimpleQueryParser getQueryParser() {
        return queryParser;
    }

    private Map<String, Object> buildListModel(Repository rep, Criteria c) {
        LOG.debug("Building list model.");
        List entities = rep.findAll(c);

        Criteria criteriaWithoutLimit = new Criteria(c.getRule());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("entities", entities);
        model.put("totalEntities", rep.findAllIds(criteriaWithoutLimit).size());
        model.put("offset", c.getLimit().getOffset());
        return model;
    }

    protected List listEntityIds(Repository rep, String query, Sorter sorter, int offset, int limit) {
        LOG.debug("Retrieving list of entities");
        Rule rule = null;
        if (query != null) {
            rule = queryParser.parse(query);
        }
        Criteria c = new Criteria(rule, sorter, new Limit(offset, limit));
        return rep.findAllIds(c);
    }
    
    protected List listEntities(Repository rep, Rule rule, Sorter sorter, int offset, int limit) {
        LOG.debug("Retrieving list of entities");
        Criteria c = new Criteria(rule, sorter, new Limit(offset, limit));
        return rep.findAll(c);
    }
    
    protected List listEntities(Repository rep, String query, Sorter sorter, int offset, int limit) {
        LOG.debug("Retrieving list of entities");
        Rule rule = null;
        if (query != null) {
            rule = queryParser.parse(query);
        }
        
        return listEntities(rep, rule, sorter, offset, limit);
    }

    protected ModelAndView listEntities(Repository rep, Sorter sorter, int offset, int limit, String view) {
        return listEntities(rep, (Rule) null, sorter, offset, limit, view);
    }
    
    protected ModelAndView listEntities(Repository rep, String query, Sorter sorter, int offset, int limit, String view) {
        Rule rule = null;
        if (query != null) {
            rule = queryParser.parse(query);
        }
        return listEntities(rep, rule, sorter, offset, limit, view);
    }
    
    protected ModelAndView listEntities(Repository rep, Rule rule, Sorter sorter, int offset, int limit, String view) {
        LOG.debug("Generating model for '{}' view.", view);
        
        Criteria c = new Criteria(rule, sorter, new Limit(offset, limit));
        return new ModelAndView(view, buildListModel(rep, c));
    }

    protected String persistEntity(Repository rep, Object entity, String redirect) {
        LOG.debug("Persisting entity followed by a redirect to '{}'.", redirect);
        rep.save(entity);
        return "redirect:" + redirect;
    }

    protected String deleteEntity(Repository rep, String id, String redirect) {
        LOG.debug("Deleting entity followed by a redirect to '{}'.", redirect);
        rep.deleteById(id);
        return "redirect:" + redirect;
    }

    @ExceptionHandler(QueryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleParsingException(QueryException ex) {
        LOG.debug("handleParsingException catching: " + ex.getMessage());
        return ex.getMessage();
    }
}
