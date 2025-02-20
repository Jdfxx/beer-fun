package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCsv(File csvFile);
}
