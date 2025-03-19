package tests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_DIR = "src/test/resources/testdata";

    /**
     * Loads JSON data from a file and converts it to the specified type
     * @param fileName The name of the file in the testdata directory
     * @param clazz The class type to convert the JSON to
     * @return The converted object
     */
    public static <T> T loadJsonData(String fileName, Class<T> clazz) {
        try {
            String filePath = Paths.get(TEST_DATA_DIR, fileName).toString();
            logger.info("Loading test data from: {}", filePath);
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            logger.error("Error loading test data from file: {}", fileName, e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    /**
     * Loads raw file content as a string
     * @param fileName The name of the file in the testdata directory
     * @return The file content as a string
     */
    public static String loadFileContent(String fileName) {
        try {
            String filePath = Paths.get(TEST_DATA_DIR, fileName).toString();
            logger.info("Loading file content from: {}", filePath);
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            logger.error("Error loading file content from: {}", fileName, e);
            throw new RuntimeException("Failed to load file content", e);
        }
    }
} 