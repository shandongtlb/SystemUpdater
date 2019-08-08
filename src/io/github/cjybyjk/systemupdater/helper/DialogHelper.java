package io.github.cjybyjk.systemupdater.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.KeyEvent;

public class DialogHelper {
    private Runnable mOnOkRunnable;
    private Runnable mOnCancelRunnable;

    public void buildDialog(Context context, String title, String message, boolean isOkOnly) {
        AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
        tDialog.setTitle(title);
        tDialog.setMessage(Html.fromHtml(message));
        tDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mOnOkRunnable != null) {
                    mOnOkRunnable.run();
                }
            }
        });
        if (!isOkOnly) {
            tDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (mOnCancelRunnable != null) {
                        mOnCancelRunnable.run();
                    }
                }
            });
        }
        tDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        tDialog.create().show();
    }

    public void buildDialog(Context context, String title, String message) {
        buildDialog(context, title, message, false);
    }

    public void buildDialog(Context context, int titleId, int messageId, boolean isOkOnly) {
        buildDialog(context, context.getString(titleId), context.getString(messageId), isOkOnly);
    }

    public void buildDialog(Context context, int titleId, int messageId) {
        buildDialog(context, titleId, messageId, false);
    }

    public void setOnOkRunnable(Runnable okRunnable) {
        mOnOkRunnable = okRunnable;
    }

    public void setOnCancelRunnable(Runnable cancelRunnable) {
        mOnCancelRunnable = cancelRunnable;
    }
}
