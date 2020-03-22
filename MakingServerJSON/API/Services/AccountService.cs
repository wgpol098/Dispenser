using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Services
{
    public class AccountService : IAccountService
    {
        private readonly IAccountRepository _accountRepository;
        private readonly IUnitOfWork _unitOfWork;

        public AccountService(IAccountRepository accountRepository, IUnitOfWork unitOfWork)
        {
            _accountRepository = accountRepository;
            _unitOfWork = unitOfWork;
        }

        public async Task<Account> CheckAccountByPass(string login)
        {
            var temp = await _accountRepository.GetTypeOfAccount(login);
            if(temp == null)
            {
                var tempacc = new Account()
                { TypeAccount = -1 };
                return tempacc;
            }
            return temp;
        }

        public async Task<AccountResponse> DeleteAsync(SentAccount sentAccount)
        {
            var existingAccount = await _accountRepository.FindByAccountAsync(sentAccount);

            if (existingAccount == null)
                return new AccountResponse("Can't find acount with that login and password.");

            try
            {
                await _accountRepository.Remove(existingAccount);
                await _unitOfWork.CompleteAsync();

                return new AccountResponse(existingAccount);
            }
            catch (Exception ex)
            {
                return new AccountResponse($"An error occurred when deleting the Account: {ex.Message}");
            }
        }

        public async Task<IEnumerable<ListOfDispenser>> GetDispensersByLoginAndPassword(string login, string password)
        {
            return await _accountRepository.GetDispensersByLoginAndPassword(login, password);
        }

        public async Task<AccountResponse> SaveAsync(Account account)
        {
            try
            {
                var check = await _accountRepository.CheckIfAccountIsInDatabase(account);

                if (check == true)
                    return new AccountResponse($"1");
                
                await _accountRepository.AddAsync(account);
                await _unitOfWork.CompleteAsync();

                return new AccountResponse(account);
            }
            catch (Exception ex)
            {
                return new AccountResponse($"An error occurred when saving the Account: {ex.Message}");
            }
        }
    }
}
