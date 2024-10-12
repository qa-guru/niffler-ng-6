package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendMapper spendMapper = new SpendMapper();

    public SpendJson createSpend(SpendJson spendJson){

        SpendEntity spendEntity = spendMapper.toEntity(spendJson);
        if(spendEntity.getCategory().getId() == null){
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return spendMapper.toDto(spendDao.create(spendEntity));

    }

}
