package com.hoolink.manage.base.dao.model;

import java.util.ArrayList;
import java.util.List;

public class ManageButtonExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_button
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_button
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_button
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     */
    public ManageButtonExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @param orderByClause
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @param distinct
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @param criteria
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     *
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * 类注释
     * GeneratedCriteria
     * 数据库表：manage_button
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andMenuIdIsNull() {
            addCriterion("menu_id is null");
            return (Criteria) this;
        }

        public Criteria andMenuIdIsNotNull() {
            addCriterion("menu_id is not null");
            return (Criteria) this;
        }

        public Criteria andMenuIdEqualTo(Long value) {
            addCriterion("menu_id =", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotEqualTo(Long value) {
            addCriterion("menu_id <>", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdGreaterThan(Long value) {
            addCriterion("menu_id >", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("menu_id >=", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdLessThan(Long value) {
            addCriterion("menu_id <", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdLessThanOrEqualTo(Long value) {
            addCriterion("menu_id <=", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdIn(List<Long> values) {
            addCriterion("menu_id in", values, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotIn(List<Long> values) {
            addCriterion("menu_id not in", values, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdBetween(Long value1, Long value2) {
            addCriterion("menu_id between", value1, value2, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotBetween(Long value1, Long value2) {
            addCriterion("menu_id not between", value1, value2, "menuId");
            return (Criteria) this;
        }

        public Criteria andButtonCodeIsNull() {
            addCriterion("button_code is null");
            return (Criteria) this;
        }

        public Criteria andButtonCodeIsNotNull() {
            addCriterion("button_code is not null");
            return (Criteria) this;
        }

        public Criteria andButtonCodeEqualTo(String value) {
            addCriterion("button_code =", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeNotEqualTo(String value) {
            addCriterion("button_code <>", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeGreaterThan(String value) {
            addCriterion("button_code >", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeGreaterThanOrEqualTo(String value) {
            addCriterion("button_code >=", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeLessThan(String value) {
            addCriterion("button_code <", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeLessThanOrEqualTo(String value) {
            addCriterion("button_code <=", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeLike(String value) {
            addCriterion("button_code like", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeNotLike(String value) {
            addCriterion("button_code not like", value, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeIn(List<String> values) {
            addCriterion("button_code in", values, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeNotIn(List<String> values) {
            addCriterion("button_code not in", values, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeBetween(String value1, String value2) {
            addCriterion("button_code between", value1, value2, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonCodeNotBetween(String value1, String value2) {
            addCriterion("button_code not between", value1, value2, "buttonCode");
            return (Criteria) this;
        }

        public Criteria andButtonNameIsNull() {
            addCriterion("button_name is null");
            return (Criteria) this;
        }

        public Criteria andButtonNameIsNotNull() {
            addCriterion("button_name is not null");
            return (Criteria) this;
        }

        public Criteria andButtonNameEqualTo(String value) {
            addCriterion("button_name =", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotEqualTo(String value) {
            addCriterion("button_name <>", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameGreaterThan(String value) {
            addCriterion("button_name >", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameGreaterThanOrEqualTo(String value) {
            addCriterion("button_name >=", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLessThan(String value) {
            addCriterion("button_name <", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLessThanOrEqualTo(String value) {
            addCriterion("button_name <=", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLike(String value) {
            addCriterion("button_name like", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotLike(String value) {
            addCriterion("button_name not like", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameIn(List<String> values) {
            addCriterion("button_name in", values, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotIn(List<String> values) {
            addCriterion("button_name not in", values, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameBetween(String value1, String value2) {
            addCriterion("button_name between", value1, value2, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotBetween(String value1, String value2) {
            addCriterion("button_name not between", value1, value2, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonUrlIsNull() {
            addCriterion("button_url is null");
            return (Criteria) this;
        }

        public Criteria andButtonUrlIsNotNull() {
            addCriterion("button_url is not null");
            return (Criteria) this;
        }

        public Criteria andButtonUrlEqualTo(String value) {
            addCriterion("button_url =", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlNotEqualTo(String value) {
            addCriterion("button_url <>", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlGreaterThan(String value) {
            addCriterion("button_url >", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlGreaterThanOrEqualTo(String value) {
            addCriterion("button_url >=", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlLessThan(String value) {
            addCriterion("button_url <", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlLessThanOrEqualTo(String value) {
            addCriterion("button_url <=", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlLike(String value) {
            addCriterion("button_url like", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlNotLike(String value) {
            addCriterion("button_url not like", value, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlIn(List<String> values) {
            addCriterion("button_url in", values, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlNotIn(List<String> values) {
            addCriterion("button_url not in", values, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlBetween(String value1, String value2) {
            addCriterion("button_url between", value1, value2, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonUrlNotBetween(String value1, String value2) {
            addCriterion("button_url not between", value1, value2, "buttonUrl");
            return (Criteria) this;
        }

        public Criteria andButtonDescIsNull() {
            addCriterion("button_desc is null");
            return (Criteria) this;
        }

        public Criteria andButtonDescIsNotNull() {
            addCriterion("button_desc is not null");
            return (Criteria) this;
        }

        public Criteria andButtonDescEqualTo(String value) {
            addCriterion("button_desc =", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescNotEqualTo(String value) {
            addCriterion("button_desc <>", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescGreaterThan(String value) {
            addCriterion("button_desc >", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescGreaterThanOrEqualTo(String value) {
            addCriterion("button_desc >=", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescLessThan(String value) {
            addCriterion("button_desc <", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescLessThanOrEqualTo(String value) {
            addCriterion("button_desc <=", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescLike(String value) {
            addCriterion("button_desc like", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescNotLike(String value) {
            addCriterion("button_desc not like", value, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescIn(List<String> values) {
            addCriterion("button_desc in", values, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescNotIn(List<String> values) {
            addCriterion("button_desc not in", values, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescBetween(String value1, String value2) {
            addCriterion("button_desc between", value1, value2, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonDescNotBetween(String value1, String value2) {
            addCriterion("button_desc not between", value1, value2, "buttonDesc");
            return (Criteria) this;
        }

        public Criteria andButtonTypeIsNull() {
            addCriterion("button_type is null");
            return (Criteria) this;
        }

        public Criteria andButtonTypeIsNotNull() {
            addCriterion("button_type is not null");
            return (Criteria) this;
        }

        public Criteria andButtonTypeEqualTo(Byte value) {
            addCriterion("button_type =", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeNotEqualTo(Byte value) {
            addCriterion("button_type <>", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeGreaterThan(Byte value) {
            addCriterion("button_type >", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("button_type >=", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeLessThan(Byte value) {
            addCriterion("button_type <", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeLessThanOrEqualTo(Byte value) {
            addCriterion("button_type <=", value, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeIn(List<Byte> values) {
            addCriterion("button_type in", values, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeNotIn(List<Byte> values) {
            addCriterion("button_type not in", values, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeBetween(Byte value1, Byte value2) {
            addCriterion("button_type between", value1, value2, "buttonType");
            return (Criteria) this;
        }

        public Criteria andButtonTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("button_type not between", value1, value2, "buttonType");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNull() {
            addCriterion("creator is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNotNull() {
            addCriterion("creator is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorEqualTo(Long value) {
            addCriterion("creator =", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotEqualTo(Long value) {
            addCriterion("creator <>", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThan(Long value) {
            addCriterion("creator >", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThanOrEqualTo(Long value) {
            addCriterion("creator >=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThan(Long value) {
            addCriterion("creator <", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThanOrEqualTo(Long value) {
            addCriterion("creator <=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorIn(List<Long> values) {
            addCriterion("creator in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotIn(List<Long> values) {
            addCriterion("creator not in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorBetween(Long value1, Long value2) {
            addCriterion("creator between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotBetween(Long value1, Long value2) {
            addCriterion("creator not between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andUpdatorIsNull() {
            addCriterion("updator is null");
            return (Criteria) this;
        }

        public Criteria andUpdatorIsNotNull() {
            addCriterion("updator is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatorEqualTo(Long value) {
            addCriterion("updator =", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorNotEqualTo(Long value) {
            addCriterion("updator <>", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorGreaterThan(Long value) {
            addCriterion("updator >", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorGreaterThanOrEqualTo(Long value) {
            addCriterion("updator >=", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorLessThan(Long value) {
            addCriterion("updator <", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorLessThanOrEqualTo(Long value) {
            addCriterion("updator <=", value, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorIn(List<Long> values) {
            addCriterion("updator in", values, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorNotIn(List<Long> values) {
            addCriterion("updator not in", values, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorBetween(Long value1, Long value2) {
            addCriterion("updator between", value1, value2, "updator");
            return (Criteria) this;
        }

        public Criteria andUpdatorNotBetween(Long value1, Long value2) {
            addCriterion("updator not between", value1, value2, "updator");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Long value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Long value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Long value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Long value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Long value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Long value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Long> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Long> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Long value1, Long value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Long value1, Long value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("updated is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("updated is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Long value) {
            addCriterion("updated =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Long value) {
            addCriterion("updated <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Long value) {
            addCriterion("updated >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Long value) {
            addCriterion("updated >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Long value) {
            addCriterion("updated <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Long value) {
            addCriterion("updated <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Long> values) {
            addCriterion("updated in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Long> values) {
            addCriterion("updated not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Long value1, Long value2) {
            addCriterion("updated between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Long value1, Long value2) {
            addCriterion("updated not between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNull() {
            addCriterion("enabled is null");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNotNull() {
            addCriterion("enabled is not null");
            return (Criteria) this;
        }

        public Criteria andEnabledEqualTo(Boolean value) {
            addCriterion("enabled =", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotEqualTo(Boolean value) {
            addCriterion("enabled <>", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThan(Boolean value) {
            addCriterion("enabled >", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThanOrEqualTo(Boolean value) {
            addCriterion("enabled >=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThan(Boolean value) {
            addCriterion("enabled <", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThanOrEqualTo(Boolean value) {
            addCriterion("enabled <=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledIn(List<Boolean> values) {
            addCriterion("enabled in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotIn(List<Boolean> values) {
            addCriterion("enabled not in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledBetween(Boolean value1, Boolean value2) {
            addCriterion("enabled between", value1, value2, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotBetween(Boolean value1, Boolean value2) {
            addCriterion("enabled not between", value1, value2, "enabled");
            return (Criteria) this;
        }
    }

    /**
     * 类注释
     * Criteria
     * 数据库表：manage_button
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * 类注释
     * Criterion
     * 数据库表：manage_button
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}