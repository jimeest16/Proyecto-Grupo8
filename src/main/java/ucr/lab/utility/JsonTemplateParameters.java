package ucr.lab.utility;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class JsonTemplateParameters implements JasperTemplateParameters {
    public JRDataSource datasource;
    public InputStream template;
    public OutputStream outputStream;
    public Map<String, Object> reportParameters;

    public JsonTemplateParameters(JRDataSource datasource, InputStream template,
                                  OutputStream outputStream, Map<String, Object> reportParameters) {
        this.datasource = datasource;
        this.template = template;
        this.outputStream = outputStream;
        this.reportParameters = reportParameters;
    }
}
