package com.spd.trello.service.helper;

import com.spd.trello.domain.CheckableItem;
import com.spd.trello.repository.impl.helper.ItemsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemsServiceHelper {
    private ItemsHelper itemsHelper;

    @Autowired
    public ItemsServiceHelper(ItemsHelper itemsHelper) {
        this.itemsHelper = itemsHelper;
    }

    public CheckableItem create(CheckableItem item) {
        return itemsHelper.create(item);
    }

    public boolean delete(UUID id) {
        return itemsHelper.delete(id);
    }

}
