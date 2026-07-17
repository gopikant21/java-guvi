class LowBalanceException(Exception):
    """Custom exception for low balance scenarios."""
    def __init__(self, message="Balance is too low to perform this operation."):
        self.message = message
        super().__init__(self.message)


try:
    balance = float(input("Enter your account balance: "))
    withdrawal_amount = float(input("Enter the amount to withdraw: "))
    
    if withdrawal_amount > balance:
        raise LowBalanceException()
    
    balance -= withdrawal_amount
    print(f"Withdrawal successful. New balance: {balance}")
except LowBalanceException as e:
    print(e)