using Microsoft.EntityFrameworkCore;
using System;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Persistence.Contexts
{
    public class AppDbContext : DbContext
    {
        public DbSet<Dispenser> Dispensers { get; set; }
        public DbSet<Account> Accounts { get; set; }
        public DbSet<Historia> History { get; set; }
        public DbSet<Plan> Plans { get; set; }
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
                new Account { Id = 1, Login = "Andrzej", Password = "Tak1223", DispenserId = 101, Dispensers = null },
                new Account { Id = 2, Login = "Moze", Password = "Byc", DispenserId = 100, Dispensers = null },
                new Account { Id = 3, Login = "Ten", Password = "Trzeci", DispenserId = 100, Dispensers = null },
                new Account { Id = 4, Login = "Oraz", Password = "Czw", DispenserId = 101, Dispensers = null },
                new Account { Id = 5, Login = "Koko", Password = "Ro", DispenserId = 102, Dispensers = null }
            );

            builder.Entity<Historia>().ToTable("History");
            builder.Entity<Historia>().HasKey(p => p.Id);
            builder.Entity<Historia>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Historia>().HasData
            (
                new Historia { Id = 1, DispenserId = 101, Datetime = DateTime.Parse("7/3/2020 12:00 AM"), Flaga = 0, Nr_Okienka = 1, Opis = "Stimea" },
                new Historia { Id = 2, DispenserId = 102, Datetime = DateTime.Parse("7/3/2020 1:00 PM"), Flaga = 1, Nr_Okienka = 2, Opis = "Iladian" },
                new Historia { Id = 3, DispenserId = 101, Datetime = DateTime.Parse("8/3/2020 4:00 AM"), Flaga = 0, Nr_Okienka = 3, Opis = "Maxigra" },
                new Historia { Id = 4, DispenserId = 103, Datetime = DateTime.Parse("8/3/2020 8:00 AM"), Flaga = 1, Nr_Okienka = 4, Opis = "Limetic" }
            );

            builder.Entity<Plan>().ToTable("Plans");
            builder.Entity<Plan>().HasKey(p => p.Id);
            builder.Entity<Plan>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Plan>().HasData
            (
                new Plan { Id = 1, DispenserId = 101, dateTime = DateTime.Parse("9/3/2020 1:00 AM"), Opis = "Stimea", Nr_Okienka = 1 },
                new Plan { Id = 2, DispenserId = 102, dateTime = DateTime.Parse("9/3/2020 2:00 PM"), Opis = "Iladian", Nr_Okienka = 2 },
                new Plan { Id = 3, DispenserId = 102, dateTime = DateTime.Parse("9/3/2020 3:00 AM"), Opis = "Maxigra", Nr_Okienka = 3 },
                new Plan { Id = 4, DispenserId = 102, dateTime = DateTime.Parse("9/3/2020 4:00 AM"), Opis = "Limetic", Nr_Okienka = 4 }
            );
        }
    }
}