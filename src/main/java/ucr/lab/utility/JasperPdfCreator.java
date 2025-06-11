package ucr.lab.utility;

import net.sf.jasperreports.engine.*;

public class JasperPdfCreator {

    public void writePdf(JasperTemplateParameters templateParams) throws JRException {
        if (!(templateParams instanceof JsonTemplateParameters jsonParams)) {
            throw new IllegalArgumentException("Tipo de parámetro no compatible");
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jsonParams.template,
                jsonParams.reportParameters,
                jsonParams.datasource
        );

        JasperExportManager.exportReportToPdfStream(jasperPrint, jsonParams.outputStream);
    }
}
