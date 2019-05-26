package com.test.kmodzelewski.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.test.kmodzelewski.globalevent.JsonEventEntry;
import com.test.kmodzelewski.globalevent.JsonMicroEvent;
import com.test.kmodzelewski.service.EventCollector;
import com.test.kmodzelewski.service.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class JsonEventProcessor implements EventProcessor {
    final Logger fileProcessorLogger = LoggerFactory.getLogger(JsonEventProcessor.class);
    private Gson gsonParesr = new Gson();
    private final EventCollector eventCollector;

    private final ApplicationContext context;
    private final ThreadPoolTaskExecutor collectTaskExecutor;

    public JsonEventProcessor(EventCollector eventCollector, ApplicationContext context, ThreadPoolTaskExecutor collectTaskExecutor) {
        this.eventCollector = eventCollector;
        this.context = context;
        this.collectTaskExecutor = collectTaskExecutor;
    }

    public void processFile(String filePath )
    {
        fileProcessorLogger.info("Start processing file: {}",filePath );

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            long lineNo = 0;
            while ((line = br.readLine()) != null)
            {
                lineNo++;
                try {
                fileProcessorLogger.trace("Got another line: {}:  [{}]",  lineNo, line);
                JsonMicroEvent jsonMicroEvent = gsonParesr.fromJson(line, JsonMicroEvent.class );
                eventCollector.collectEventData( new JsonEventEntry( line,jsonMicroEvent ));
                if ( lineNo % 10000 == 0 )
                {
                    fileProcessorLogger.info("Processed {} lines", lineNo);
                }
                } catch ( JsonSyntaxException jse )
                {
                    fileProcessorLogger.warn("Could not parse entry at line: {}, {}", lineNo, jse.getMessage() );
                    //TODO: register unprocessed line
                } catch ( Exception e )
                {
                    fileProcessorLogger.warn("Could not process file entry at line: {}, error: {}", lineNo, e.getMessage());
                }
            }
        } catch (IOException e) {
            fileProcessorLogger.error("Could not read file [{}] error: {}", filePath, e.getMessage(), e);
           throw  new RuntimeException();
        }

        fileProcessorLogger.info("End processing file: {}",filePath );
    }
}
