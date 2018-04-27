package ru.mail.park.cherkov.db.utils.sqlGenerators;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetSortSql {

    FlatSort flatSort;
    ParentTreeSort parentTreeSort;
    TreeSort treeSort;

    public GetSortSql(FlatSort flatSort, ParentTreeSort parentTreeSort, TreeSort treeSort) {
        this.flatSort = flatSort;
        this.parentTreeSort = parentTreeSort;
        this.treeSort = treeSort;
    }

    public String get(
            String sortType,
            ArrayList<Object> destArgs,
            Long threadId,
            Integer limit,
            Integer since,
            Boolean desc
    ) {
        switch (sortType) {
            case "tree":
                return treeSort.getSql(
                        destArgs, threadId, limit, since, desc);
            case "parent_tree":
                return parentTreeSort.getSql(
                        destArgs, threadId, limit, since, desc);
            default:
                break;
        }
        return flatSort.getSql(
                destArgs, threadId, limit, since, desc);
    }


}
