package dk.apaq.simplepay.controllers.site;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Javadoc
 */
@Controller
public class DocsController {

    private static final String DOCUMENTATION_PATH = "/WEB-INF/views/docs";
    private List<String> languages = Arrays.asList(new String[]{"en", "da"});
    private String defaultLanguage = "en";

    public class Section {

        private final String name;
        private final List<String> pages;

        public Section(String name, List<String> pages) {
            this.name = name;
            this.pages = pages;
        }

        public String getName() {
            return name;
        }

        public List<String> getPages() {
            return pages;
        }
    }
    
    @RequestMapping(value = {"/docs/index.html"})
    public ModelAndView handleRequest(HttpServletRequest req) {
        return handleRequest(req, null, null);
    }
    

    @RequestMapping(value = {"/docs/{section}/{page}.html"})
    public ModelAndView handleRequest(HttpServletRequest req, @PathVariable String section, @PathVariable String page) {
        String language = req.getLocale().getLanguage();

        if (!languages.contains(language)) {
            language = defaultLanguage;
        }

        if(section == null || page == null) {
            section = "documentation";
            page = "getting_started";
        }
        
        String docfile = section + "/" + page;
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("docfile", docfile);
        model.put("language", language);
        model.put("menusections", buildMenuSections(req, language));
        return new ModelAndView("doc", model);
    }

    private List<Section> buildMenuSections(HttpServletRequest request, String language) {
        List<Section> sections = new ArrayList<Section>();
        String docpath = request.getServletContext().getRealPath(DOCUMENTATION_PATH);
        File docFolder = new File(docpath);

        File[] docFolders = docFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        for (File file : docFolders) {
            sections.add(buildSection(file, language));
        }
        return sections;
    }

    private Section buildSection(File docFolder, String language) {
        String suffix = "_" + language + ".jsp";
        String name = docFolder.getName();
        List<String> pages = new ArrayList<String>();

        for (File file : docFolder.listFiles()) {
            if (file.getName().endsWith(suffix)) {
                String filename = file.getName();
                pages.add(filename.substring(0, filename.length() - suffix.length()));
            }
        }

        return new Section(name, pages);
    }
}
