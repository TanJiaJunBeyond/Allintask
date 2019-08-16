package com.allintask.lingdao.utils;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class MailboxUtils {

    public static boolean isMailbox(String mailbox) {
        String mailboxRegax = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

        if (mailbox.matches(mailboxRegax)) {
            return true;
        } else {
            return false;
        }
    }

}
