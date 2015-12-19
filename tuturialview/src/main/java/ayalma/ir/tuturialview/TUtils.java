package ayalma.ir.tuturialview;
import android.content.Context;
/**
 * Created by alimohammadi on 12/17/15.
 */
final class TUtils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
