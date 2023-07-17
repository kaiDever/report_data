package xinmei.report.data.dto;

import java.util.List;

public class DataResponse {
    private List<List<Object>> rows;
    private List<Object> columns;

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public List<Object> getColumns() {
        return columns;
    }

    public void setColumns(List<Object> columns) {
        this.columns = columns;
    }
}
