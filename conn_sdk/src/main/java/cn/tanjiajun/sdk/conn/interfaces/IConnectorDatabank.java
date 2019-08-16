package cn.tanjiajun.sdk.conn.interfaces;

/**
 * 网络返回结果缓存接口
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public interface IConnectorDatabank {

    String getData(String request);

    void saveData(String request, String response);

    void deleteData();

    void closeDatabase();

}
