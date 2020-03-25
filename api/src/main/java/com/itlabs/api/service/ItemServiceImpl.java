package com.itlabs.api.service;

import com.itlabs.api.entity.Item;
import com.itlabs.api.models.ItemEditModel;
import com.itlabs.api.models.ItemModel;
import com.itlabs.api.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;

  public ItemServiceImpl(ItemRepository itemRepository) {

    this.itemRepository = itemRepository;
  }

  /**
   * @param id
   * @return ItemModel
   * @throws EmptyResultDataAccessException
   */
  @Override
  public ItemModel get(Integer id) {
    Item item = getDatabaseItem(id);
    var result = getModel(item);
    return result;
  }

  /**
   * @param pageable
   * @return List<ItemModel>
   */
  @Override
  public List<ItemModel> get(Pageable pageable) {
    return itemRepository.findAll(pageable).stream()
        .map(this::getModel)
        .collect(Collectors.toList());
  }
  /**
   * @param model
   * @return ItemModel
   */
  @Override
  public ItemModel save(ItemEditModel model) {
    var item = new Item();
    item.setName(model.getName());
    item.setStatus(model.getStatus());
    item.setType("PERSONAL");
    item.setDescription(model.getDescription());
    return getModel(itemRepository.save(item));
  }

  /**
   * @param id
   * @param editModel
   * @return ItemModel
   * @throws EmptyResultDataAccessException
   */
  @Override
  public ItemModel update(int id, ItemEditModel editModel) {
    var item = getDatabaseItem(id);
    item.setName(editModel.getName());
    item.setStatus(editModel.getStatus());
    item.setDescription(editModel.getDescription());
    return getModel(itemRepository.save(item));
  }

  /** @param id */
  @Override
  public void delete(int id) {
    itemRepository.deleteById(id);
  }

  private Item getDatabaseItem(Integer id) {
    return itemRepository
        .findById(id)
        .orElseThrow(
            () ->
                new EmptyResultDataAccessException(
                    String.format("Item with id %d not found", id), 1));
  }

  private ItemModel getModel(Item item) {
    return ItemModel.builder()
        .id(item.getId())
        .name(item.getName())
        .description(item.getDescription())
        .status(item.getStatus())
        .build();
  }
}