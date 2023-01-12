package com.wilo.springbootjpamysql.app.view.csv;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("list.csv")
public class ClientCsvView extends AbstractView {

    public ClientCsvView() {
        setContentType("text/csv");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"client.csv\"");
        response.setContentType(getContentType());
        Page<Client> clients = (Page<Client>) model.get("clients");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] header = {"id", "name", "first_name", "email", "createAt"};
        beanWriter.writeHeader(header);

        // guardarmos cada objeto en el archivo csv
        for (Client client : clients) {
            beanWriter.write(client, header);
        }
        beanWriter.close();
    }
}
