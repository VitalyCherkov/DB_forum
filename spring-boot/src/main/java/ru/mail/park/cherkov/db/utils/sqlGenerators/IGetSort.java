package ru.mail.park.cherkov.db.utils.sqlGenerators;

import java.util.ArrayList;

public interface IGetSort {

    public String getSql(
            ArrayList<Object> destArgs,
            Long threadId,
            Integer limit,
            Integer since,
            Boolean desc
            );

}
