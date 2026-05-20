package transactionmanager.challange.util;

import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;

public final class Factory {
  private Factory() {}

  public static Transaction aTransaction() {
    return Transaction.builder().id(1L).amount(100.0).type("DEBIT").parentId(null).build();
  }

  public static Transaction aTransaction(Long id) {
    return aTransaction().toBuilder().id(id).build();
  }

  public static TransactionEntity aTransactionEntity() {
    return TransactionEntity.builder().id(1L).amount(100.0).type("DEBIT").parentId(null).build();
  }

  public static TransactionEntity aTransactionEntity(Long id) {
    return aTransactionEntity().toBuilder().id(id).build();
  }

  public static TransactionEntity aTransactionEntityWithoutId() {
    return TransactionEntity.builder().amount(100.0).type("DEBIT").build();
  }
}

