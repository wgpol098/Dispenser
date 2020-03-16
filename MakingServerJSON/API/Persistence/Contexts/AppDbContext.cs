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

            builder.Entity<Dispenser>().HasData
            (
                new Dispenser { Id = 1, DispenserId = 101, Nr_Okienka = "1010011" },
                new Dispenser { Id = 2, DispenserId = 102, Nr_Okienka = "1001001" },
                new Dispenser { Id = 4, DispenserId = 103, Nr_Okienka = "1011101" },
                new Dispenser { Id = 5, DispenserId = 104, Nr_Okienka = "1001001" },
                new Dispenser { Id = 6, DispenserId = 105, Nr_Okienka = "0001000" },
                new Dispenser { Id = 7, DispenserId = 106, Nr_Okienka = "0001001" },
                new Dispenser { Id = 8, DispenserId = 107, Nr_Okienka = "0001100" },
                new Dispenser { Id = 9, DispenserId = 108, Nr_Okienka = "0101010" }
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

            builder.Entity<Historia>().ToTable("History");
            builder.Entity<Historia>().HasKey(p => p.Id);
            builder.Entity<Historia>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Historia>().HasData
            (
                new Historia { Id = 1, DispenserId = 101, DateAndTime = DateTime.Parse("7/3/2020 12:00 AM"), Flaga = 0, Nr_Okienka = 1, Opis = "Stimea" },
                new Historia { Id = 2, DispenserId = 102, DateAndTime = DateTime.Parse("7/3/2020 1:00 PM"), Flaga = 1, Nr_Okienka = 2, Opis = "Iladian" },
                new Historia { Id = 3, DispenserId = 101, DateAndTime = DateTime.Parse("8/3/2020 4:00 AM"), Flaga = 0, Nr_Okienka = 3, Opis = "Maxigra" },
                new Historia { Id = 4, DispenserId = 103, DateAndTime = DateTime.Parse("8/3/2020 8:00 AM"), Flaga = 1, Nr_Okienka = 4, Opis = "Limetic" }
            );

            builder.Entity<Plan>().ToTable("Plans");
            builder.Entity<Plan>().HasKey(p => p.Id);
            builder.Entity<Plan>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Plan>().HasData
            (
            /*new Plan { Id = 1, DispenserId = 101, DateAndTime = DateTime.Parse("9/3/2020 1:00 AM"), Opis = "Stimea", Nr_Okienka = 1 },
            new Plan { Id = 2, DispenserId = 102, DateAndTime = DateTime.Parse("9/3/2020 2:00 PM"), Opis = "Iladian", Nr_Okienka = 2 },
            new Plan { Id = 3, DispenserId = 102, DateAndTime = DateTime.Parse("9/3/2020 3:00 AM"), Opis = "Maxigra", Nr_Okienka = 3 },
            new Plan { Id = 4, DispenserId = 102, DateAndTime = DateTime.Parse("9/3/2020 4:00 AM"), Opis = "Limetic", Nr_Okienka = 4 }*/
                new Plan { Id = 1, DispenserId = 102, DateAndTime = DateTime.Parse("9/3/2020 1:00 AM"), Opis = "Apap", Nr_Okienka = 1 },
                new Plan { Id = 2, DispenserId = 102, DateAndTime = DateTime.Parse("10/3/2020 2:00 PM"), Opis = "Apap", Nr_Okienka = 2 },
                new Plan { Id = 3, DispenserId = 102, DateAndTime = DateTime.Parse("11/3/2020 3:00 AM"), Opis = "Apap", Nr_Okienka = 3 },
                new Plan { Id = 4, DispenserId = 102, DateAndTime = DateTime.Parse("9/3/2020 4:00 AM"), Opis = "IBUM", Nr_Okienka = 4 },
                new Plan { Id = 5, DispenserId = 102, DateAndTime = DateTime.Parse("10/3/2020 4:00 AM"), Opis = "IBUM", Nr_Okienka = 5 },
                new Plan { Id = 6, DispenserId = 102, DateAndTime = DateTime.Parse("11/3/2020 4:00 AM"), Opis = "IBUM", Nr_Okienka = 6 }
            );
        }
    }
}