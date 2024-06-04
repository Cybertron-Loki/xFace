package ming.test.xface.common;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.util.SaResult;
import com.google.gson.Gson;

/**
 * 返回工具类
 */
public class ResultUtils {

    private static Gson gson = new Gson();

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static SaResult success(Object data) {
        return new SaResult(0, gson.toJson(data), "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static SaResult error(ErrorCode errorCode) {
        return new SaResult(errorCode.getCode(), null, errorCode.getMessage());
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static SaResult error(int code, String message) {
        return new SaResult(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static SaResult error(ErrorCode errorCode, String message) {
        return new SaResult(errorCode.getCode(), null, message);
    }
}
