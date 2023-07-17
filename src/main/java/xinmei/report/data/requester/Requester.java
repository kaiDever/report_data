package xinmei.report.data.requester;

import xinmei.report.data.dto.DataResponse;

/**
 * DateResponse -> file
 */
public interface Requester {
    void save(DataResponse dataResponse,String sheetName);
}
