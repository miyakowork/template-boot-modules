package me.wuwenbin.modules.scanner;

import me.wuwenbin.modules.scanner.config.ScannerConfig;
import me.wuwenbin.modules.scanner.persistence.ScannerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * created by Wuwenbin on 2017/12/26 at 13:27
 */
public class ResourceScanListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger LOG = LoggerFactory.getLogger(ResourceScanListener.class);

    private ScannerConfig scannerConfig;
    private ScannerRepository scannerRepository;

    public ResourceScanListener(ScannerConfig config, ScannerRepository repository) {
        this.scannerConfig = config;
        this.scannerRepository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            Scanner.scan(scannerRepository, scannerConfig, contextRefreshedEvent);
        } catch (Exception e) {
            LOG.error("初始化扫描失败！", e);
        }
    }
}
