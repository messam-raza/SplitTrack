package com.messamraza.splittrack.database

import androidx.room.*

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getAllExpenses(): List<Expense>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    suspend fun getExpensesByCategory(category: String): List<Expense>
}

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups ORDER BY createdDate DESC")
    suspend fun getAllGroups(): List<Group>

    @Insert
    suspend fun insertGroup(group: Group): Long

    @Query("SELECT * FROM group_members WHERE groupId = :groupId")
    suspend fun getGroupMembers(groupId: Long): List<GroupMember>

    @Insert
    suspend fun insertGroupMember(member: GroupMember)

    @Query("SELECT * FROM group_expenses WHERE groupId = :groupId ORDER BY date DESC")
    suspend fun getGroupExpenses(groupId: Long): List<GroupExpense>

    @Insert
    suspend fun insertGroupExpense(expense: GroupExpense)

    @Delete
    suspend fun deleteGroup(group: Group)
}
