package com.felxx.park_rest_api.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {
    
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;

    private Map<String, Object> parameters = new HashMap<>();

    private static final String REPORT_PATH = "classpath:reports/";

    public void addParams(String key, Object value) {
        this.parameters.put("IMAGEM_DIRETORIO", REPORT_PATH);
        this.parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
        this.parameters.put(key, value);
    }

    public byte[] generateReport() {
        byte[] bytes = null;
        try {
            Resource resource = resourceLoader.getResource(REPORT_PATH + "park_report.jasper");
            InputStream stream = resource.getInputStream();
            JasperPrint print = JasperFillManager.fillReport(stream, parameters, dataSource.getConnection());
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            log.error("Error generating report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate report", e);
        }
        return bytes;
    }
}
