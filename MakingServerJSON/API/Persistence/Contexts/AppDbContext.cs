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
                new Dispenser { Id = 1, DispenserId = 101, Nr_Okienka = "1110000" },
                new Dispenser { Id = 2, DispenserId = 102, Nr_Okienka = "1010100" },
                new Dispenser { Id = 3, DispenserId = 103, Nr_Okienka = "0100010" },
                new Dispenser { Id = 4, DispenserId = 104, Nr_Okienka = "1000000" },
                new Dispenser { Id = 5, DispenserId = 105, Nr_Okienka = "0000001" },
                new Dispenser { Id = 6, DispenserId = 106, Nr_Okienka = "0001000" },
                new Dispenser { Id = 7, DispenserId = 107, Nr_Okienka = "1000001" },
                new Dispenser { Id = 8, DispenserId = 108, Nr_Okienka = "0001001" },
                new Dispenser { Id = 9, DispenserId = 109, Nr_Okienka = "1000010" }
            );

            builder.Entity<Account>().ToTable("Accounts");
            builder.Entity<Account>().HasKey(p => p.Id);
            builder.Entity<Account>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Account>().HasData
            (
                new Account { Id = 1, Login = "AndrzejKrawczyk", Password = "Tak123", DispenserId = 101 },
                new Account { Id = 2, Login = "MateuszWałek", Password = "Nie321", DispenserId = 102 },
                new Account { Id = 3, Login = "TomekKowalski", Password = "Haslo", DispenserId = 103 },
                new Account { Id = 4, Login = "PawełNowak", Password = "Test", DispenserId = 104 },
                new Account { Id = 5, Login = "JakubLewandowski", Password = "Somsiad", DispenserId = 105 },
                new Account { Id = 6, Login = "MarekPracowity", Password = "Viagra", DispenserId = 106 },
                new Account { Id = 7, Login = "FranekGolas", Password = "Wojna", DispenserId = 107 },
                new Account { Id = 8, Login = "AdamMickiewicz", Password = "Pisarz", DispenserId = 108 },
                new Account { Id = 9, Login = "JanKazimierzKretacz", Password = "Test109", DispenserId = 109 }
            );

            builder.Entity<Historia>().ToTable("History");
            builder.Entity<Historia>().HasKey(p => p.Id);
            builder.Entity<Historia>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Historia>().HasData
            (
                new Historia { Id = 1, DispenserId = 109, DateAndTime = DateTime.Parse("1/3/2020 9:00 AM"), Flaga = 0, Nr_Okienka = 1, Opis = "Stimea" },
                new Historia { Id = 2, DispenserId = 108, DateAndTime = DateTime.Parse("1/3/2020 11:00 AM"), Flaga = 1, Nr_Okienka = 2, Opis = "Iladian" },
                new Historia { Id = 3, DispenserId = 107, DateAndTime = DateTime.Parse("1/3/2020 4:00 PM"), Flaga = 0, Nr_Okienka = 3, Opis = "Maxigra" },
                new Historia { Id = 4, DispenserId = 106, DateAndTime = DateTime.Parse("2/3/2020 6:00 AM"), Flaga = 1, Nr_Okienka = 4, Opis = "Limetic" },
                new Historia { Id = 5, DispenserId = 105, DateAndTime = DateTime.Parse("2/3/2020 9:00 AM"), Flaga = 1, Nr_Okienka = 5, Opis = "Stimea" },
                new Historia { Id = 6, DispenserId = 104, DateAndTime = DateTime.Parse("2/3/2020 6:00 PM"), Flaga = -1, Nr_Okienka = 6, Opis = "Iladian" },
                new Historia { Id = 7, DispenserId = 103, DateAndTime = DateTime.Parse("3/3/2020 8:00 AM"), Flaga = 1, Nr_Okienka = 7, Opis = "Maxigra" },
                new Historia { Id = 8, DispenserId = 102, DateAndTime = DateTime.Parse("3/3/2020 1:00 PM"), Flaga = 1, Nr_Okienka = 1, Opis = "Limetic" },
                new Historia { Id = 9, DispenserId = 101, DateAndTime = DateTime.Parse("3/3/2020 5:00 PM"), Flaga = 1, Nr_Okienka = 1, Opis = "Stimea" },
                new Historia { Id = 10, DispenserId = 102, DateAndTime = DateTime.Parse("4/3/2020 9:00 AM"), Flaga = -1, Nr_Okienka = 2, Opis = "Iladian" },
                new Historia { Id = 11, DispenserId = 103, DateAndTime = DateTime.Parse("4/3/2020 2:00 PM"), Flaga = 1, Nr_Okienka = 2, Opis = "Maxigra" },
                new Historia { Id = 12, DispenserId = 104, DateAndTime = DateTime.Parse("4/3/2020 6:00 PM"), Flaga = 0, Nr_Okienka = 3, Opis = "Limetic" },
                new Historia { Id = 13, DispenserId = 105, DateAndTime = DateTime.Parse("5/3/2020 7:00 AM"), Flaga = 1, Nr_Okienka = 3, Opis = "Stimea" },
                new Historia { Id = 14, DispenserId = 106, DateAndTime = DateTime.Parse("5/3/2020 8:00 AM"), Flaga = -1, Nr_Okienka = 4, Opis = "Iladian" },
                new Historia { Id = 15, DispenserId = 107, DateAndTime = DateTime.Parse("5/3/2020 5:00 PM"), Flaga = 1, Nr_Okienka = 5, Opis = "Maxigra" },
                new Historia { Id = 16, DispenserId = 108, DateAndTime = DateTime.Parse("6/3/2020 8:00 AM"), Flaga = 1, Nr_Okienka = 6, Opis = "Limetic" },
                new Historia { Id = 17, DispenserId = 109, DateAndTime = DateTime.Parse("6/3/2020 9:00 PM"), Flaga = 1, Nr_Okienka = 7, Opis = "Stimea" },
                new Historia { Id = 18, DispenserId = 109, DateAndTime = DateTime.Parse("7/3/2020 10:00 AM"), Flaga = -1, Nr_Okienka = 1, Opis = "Maxigra" },
                new Historia { Id = 19, DispenserId = 109, DateAndTime = DateTime.Parse("7/3/2020 9:00 PM"), Flaga = 1, Nr_Okienka = 2, Opis = "Iladian" }
            );

            builder.Entity<Plan>().ToTable("Plans");
            builder.Entity<Plan>().HasKey(p => p.Id);
            builder.Entity<Plan>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Plan>().HasData
            (
                new Plan { Id = 1, DispenserId = 101, DateAndTime = DateTime.Parse("20/3/2020 1:00 PM"), Opis = "Apap", Nr_Okienka = 1 },
                new Plan { Id = 2, DispenserId = 101, DateAndTime = DateTime.Parse("21/3/2020 1:00 PM"), Opis = "Apap", Nr_Okienka = 2 },
                new Plan { Id = 3, DispenserId = 101, DateAndTime = DateTime.Parse("22/3/2020 1:00 PM"), Opis = "Apap", Nr_Okienka = 3 },
                new Plan { Id = 4, DispenserId = 102, DateAndTime = DateTime.Parse("20/3/2020 6:00 AM"), Opis = "IBUM", Nr_Okienka = 1 },
                new Plan { Id = 5, DispenserId = 102, DateAndTime = DateTime.Parse("20/3/2020 6:00 PM"), Opis = "IBUM", Nr_Okienka = 3 },
                new Plan { Id = 6, DispenserId = 102, DateAndTime = DateTime.Parse("21/3/2020 6:00 AM"), Opis = "IBUM", Nr_Okienka = 5 },
                new Plan { Id = 7, DispenserId = 103, DateAndTime = DateTime.Parse("20/3/2020 4:00 AM"), Opis = "aaa", Nr_Okienka = 2 },
                new Plan { Id = 8, DispenserId = 103, DateAndTime = DateTime.Parse("20/3/2020 11:00 PM"), Opis = "aaa", Nr_Okienka = 6 },
                new Plan { Id = 9, DispenserId = 104, DateAndTime = DateTime.Parse("20/3/2020 5:00 AM"), Opis = "bbb", Nr_Okienka = 1 },
                new Plan { Id = 10, DispenserId = 105, DateAndTime = DateTime.Parse("20/3/2020 4:00 AM"), Opis = "ccc", Nr_Okienka = 7 },
                new Plan { Id = 11, DispenserId = 106, DateAndTime = DateTime.Parse("25/3/2020 9:00 AM"), Opis = "ddd", Nr_Okienka = 4 },
                new Plan { Id = 12, DispenserId = 107, DateAndTime = DateTime.Parse("20/3/2020 1:00 AM"), Opis = "brain", Nr_Okienka = 1 },
                new Plan { Id = 13, DispenserId = 107, DateAndTime = DateTime.Parse("20/3/2020 2:00 AM"), Opis = "brain", Nr_Okienka = 7 },
                new Plan { Id = 14, DispenserId = 108, DateAndTime = DateTime.Parse("20/3/2020 5:00 AM"), Opis = "lol", Nr_Okienka = 4 },
                new Plan { Id = 15, DispenserId = 108, DateAndTime = DateTime.Parse("22/3/2020 4:00 PM"), Opis = "lol", Nr_Okienka = 7 },
                new Plan { Id = 16, DispenserId = 109, DateAndTime = DateTime.Parse("20/3/2020 4:00 PM"), Opis = "Eutanazol", Nr_Okienka = 1 },
                new Plan { Id = 17, DispenserId = 109, DateAndTime = DateTime.Parse("20/3/2020 5:30 PM"), Opis = "Eutanazol", Nr_Okienka = 6 }
            );
        }
    }
}