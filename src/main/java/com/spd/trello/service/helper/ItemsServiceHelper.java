package com.spd.trello.service.helper;

import com.spd.trello.domain.CheckableItem;
import com.spd.trello.repository.impl.helper.ItemsHelper;

import java.util.UUID;

public class ItemsServiceHelper {
    private ItemsHelper itemsHelper;

    public ItemsServiceHelper() {
        this.itemsHelper = new ItemsHelper();
    }

    public CheckableItem create(CheckableItem item) {
        return itemsHelper.create(item);
    }

    public boolean delete(UUID id) {
        return itemsHelper.delete(id);
    }

}
