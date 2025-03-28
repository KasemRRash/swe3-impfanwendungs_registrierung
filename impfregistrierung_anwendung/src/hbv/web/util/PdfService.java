package hbv.web.util;

import java.io.File;
import java.io.IOException;

public interface PdfService {
  File generatePDF(String bookingId, String datum, String impfzentrum) throws IOException;
}
