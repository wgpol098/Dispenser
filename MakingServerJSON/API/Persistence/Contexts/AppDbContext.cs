using Microsoft.EntityFrameworkCore;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Persistence.Contexts
{
    public class AppDbContext : DbContext
    {
        public DbSet<Dispenser> Dispensers { get; set; }
        public DbSet<Account> Accounts { get; set; }
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {}

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<Dispenser>().ToTable("Dispensers");
            builder.Entity<Dispenser>().HasKey(p => p.Id);
            builder.Entity<Dispenser>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();
            builder.Entity<Dispenser>().Property(p => p.Name).IsRequired().HasMaxLength(30);

            builder.Entity<Dispenser>().HasData
            (
                new Dispenser { Id = 100, Name = "Cos", TurnDiode = false },
                new Dispenser { Id = 101, Name = "Tam", TurnDiode = true},
                new Dispenser { Id = 102, Name = "Yayy", TurnDiode = false}
            );

            builder.Entity<Account>().ToTable("Accounts");
            builder.Entity<Account>().HasKey(p => p.Id);
            builder.Entity<Account>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Account>().HasData
            (
                new Account { Id = 1, Login = "Andrzej", Password = "Tak1223", DispenserId = 101 },
                new Account { Id = 2, Login = "Moze", Password = "Byc", DispenserId = 100 },
                new Account { Id = 3, Login = "Ten", Password = "Trzeci", DispenserId = 100 },
                new Account { Id = 4, Login = "Oraz", Password = "Czw", DispenserId = 101 },
                new Account { Id = 5, Login = "Koko", Password = "Ro", DispenserId = 102 }
            );
        }
    }
}
