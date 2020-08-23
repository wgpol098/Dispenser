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
        public DbSet<Presentation> Presentations { get; set; }
        public DbSet<ListOfDispenser> ListOfDispensers { get; set; }
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
                new Dispenser { Id = 1, IdDispenser = 101, NoWindow = "1110000", NoUpdate = 5 },
                new Dispenser { Id = 2, IdDispenser = 102, NoWindow = "1111100", NoUpdate = 10 },
                new Dispenser { Id = 3, IdDispenser = 103, NoWindow = "0100010", NoUpdate = 15 },
                new Dispenser { Id = 4, IdDispenser = 104, NoWindow = "1000000", NoUpdate = 20 },
                new Dispenser { Id = 5, IdDispenser = 105, NoWindow = "0000001", NoUpdate = 25 },
                new Dispenser { Id = 6, IdDispenser = 106, NoWindow = "0001000", NoUpdate = 30 },
                new Dispenser { Id = 7, IdDispenser = 107, NoWindow = "1000001", NoUpdate = 35 },
                new Dispenser { Id = 8, IdDispenser = 108, NoWindow = "0001001", NoUpdate = 40 },
                new Dispenser { Id = 9, IdDispenser = 109, NoWindow = "1000010", NoUpdate = 45 }
            );

            builder.Entity<Account>().ToTable("Accounts");
            builder.Entity<Account>().HasKey(p => p.Id);
            builder.Entity<Account>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Account>().HasData
            (
                new Account { Id = 1, Login = "TestGuy123", Password = "Tak123", Name = "Andrzej Krawczyk", TypeAccount = 1},
                new Account { Id = 2, Login = "TG123456", Password = "Nie321", Name = "Mateusz Wałek", TypeAccount = 1 },
                new Account { Id = 3, Login = "TTeesstt", Password = "Haslo", Name = "Tomek Kowalski", TypeAccount = 1 },
                new Account { Id = 4, Login = "Tetestst", Password = "Test", Name = "Paweł Nowak", TypeAccount = 1 },
                new Account { Id = 5, Login = "GuyGuyGuy", Password = "Somsiad", Name = "Jakub Lewandowski", TypeAccount = 1 },
                new Account { Id = 6, Login = "TempTemp", Password = "Viagra", Name = "Marek Pracowity", TypeAccount = 3 },
                new Account { Id = 7, Login = "TestAccount", Password = "Wojna", Name = "Franek Golas", TypeAccount = 2 },
                new Account { Id = 8, Login = "qwerty", Password = "Pisarz", Name = "Adam Mickiewicz", TypeAccount = 2 },
                new Account { Id = 9, Login = "qwerty123", Password = "Test109", Name = "Jan Kazimierz Kretacz", TypeAccount = 1 }
            );

            builder.Entity<Historia>().ToTable("History");
            builder.Entity<Historia>().HasKey(p => p.Id);
            builder.Entity<Historia>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Historia>().HasData
            (
                new Historia { Id = 1, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-1 09:00:01"), Flag = 0, NoWindow = 1, Description = "Stimea" },
                new Historia { Id = 2, IdDispenser = 108, DateAndTime = DateTime.Parse("2020-03-1 11:00:01"), Flag = 1, NoWindow = 2, Description = "Iladian" },
                new Historia { Id = 3, IdDispenser = 107, DateAndTime = DateTime.Parse("2020-03-1 16:00:01"), Flag = 0, NoWindow = 3, Description = "Maxigra" },
                new Historia { Id = 4, IdDispenser = 106, DateAndTime = DateTime.Parse("2020-03-2 06:00:01"), Flag = 1, NoWindow = 4, Description = "Limetic" },
                new Historia { Id = 5, IdDispenser = 105, DateAndTime = DateTime.Parse("2020-03-2 09:00:01"), Flag = 1, NoWindow = 5, Description = "Stimea" },
                new Historia { Id = 6, IdDispenser = 104, DateAndTime = DateTime.Parse("2020-03-2 18:00:01"), Flag = -1, NoWindow = 6, Description = "Iladian" },
                new Historia { Id = 7, IdDispenser = 103, DateAndTime = DateTime.Parse("2020-03-3 08:00:01"), Flag = 1, NoWindow = 7, Description = "Maxigra" },
                new Historia { Id = 8, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-3 13:00:01"), Flag = 1, NoWindow = 1, Description = "Limetic" },
                new Historia { Id = 9, IdDispenser = 101, DateAndTime = DateTime.Parse("2020-03-3 17:00:01"), Flag = 1, NoWindow = 1, Description = "Stimea" },
                new Historia { Id = 10, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-4 09:00:01"), Flag = -1, NoWindow = 2, Description = "Iladian" },
                new Historia { Id = 11, IdDispenser = 103, DateAndTime = DateTime.Parse("2020-03-4 14:00:01"), Flag = 1, NoWindow = 2, Description = "Maxigra" },
                new Historia { Id = 12, IdDispenser = 104, DateAndTime = DateTime.Parse("2020-03-4 18:00:01"), Flag = 0, NoWindow = 3, Description = "Limetic" },
                new Historia { Id = 13, IdDispenser = 105, DateAndTime = DateTime.Parse("2020-03-5 07:00:01"), Flag = 1, NoWindow = 3, Description = "Stimea" },
                new Historia { Id = 14, IdDispenser = 106, DateAndTime = DateTime.Parse("2020-03-5 08:00:01"), Flag = -1, NoWindow = 4, Description = "Iladian" },
                new Historia { Id = 15, IdDispenser = 107, DateAndTime = DateTime.Parse("2020-03-5 17:00:01"), Flag = 1, NoWindow = 5, Description = "Maxigra" },
                new Historia { Id = 16, IdDispenser = 108, DateAndTime = DateTime.Parse("2020-03-6 08:00:01"), Flag = 1, NoWindow = 6, Description = "Limetic" },
                new Historia { Id = 17, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-6 21:00:01"), Flag = 1, NoWindow = 7, Description = "Stimea" },
                new Historia { Id = 18, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-7 10:00:01"), Flag = -1, NoWindow = 1, Description = "Maxigra" },
                new Historia { Id = 19, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-7 21:00:01"), Flag = 1, NoWindow = 2, Description = "Iladian" }
            );

            builder.Entity<Plan>().ToTable("Plans");
            builder.Entity<Plan>().HasKey(p => p.Id);
            builder.Entity<Plan>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Plan>().HasData
            (
                new Plan { Id = 1, IdDispenser = 101, DateAndTime = DateTime.Parse("2020-03-20 13:00:01"), Description = "Apap", NoWindow = 1 },
                new Plan { Id = 2, IdDispenser = 101, DateAndTime = DateTime.Parse("2020-03-21 13:00:01"), Description = "Apap", NoWindow = 2 },
                new Plan { Id = 3, IdDispenser = 101, DateAndTime = DateTime.Parse("2020-03-22 13:00:01"), Description = "Apap", NoWindow = 3 },
                new Plan { Id = 4, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-20 06:00:01"), Description = "IBUM", NoWindow = 1 },
                new Plan { Id = 5, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-20 18:00:01"), Description = "IBUM", NoWindow = 3 },
                new Plan { Id = 6, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-21 06:00:01"), Description = "IBUM", NoWindow = 5 },
                new Plan { Id = 7, IdDispenser = 103, DateAndTime = DateTime.Parse("2020-03-20 04:00:01"), Description = "aaa", NoWindow = 2 },
                new Plan { Id = 8, IdDispenser = 103, DateAndTime = DateTime.Parse("2020-03-20 23:00:01"), Description = "aaa", NoWindow = 6 },
                new Plan { Id = 9, IdDispenser = 104, DateAndTime = DateTime.Parse("2020-03-20 05:00:01"), Description = "bbb", NoWindow = 1 },
                new Plan { Id = 10, IdDispenser = 105, DateAndTime = DateTime.Parse("2020-03-20 04:00:01"), Description = "ccc", NoWindow = 7 },
                new Plan { Id = 11, IdDispenser = 106, DateAndTime = DateTime.Parse("2020-03-25 09:00:01"), Description = "ddd", NoWindow = 4 },
                new Plan { Id = 12, IdDispenser = 107, DateAndTime = DateTime.Parse("2020-03-20 01:00:01"), Description = "brain", NoWindow = 1 },
                new Plan { Id = 13, IdDispenser = 107, DateAndTime = DateTime.Parse("2020-03-20 02:00:01"), Description = "brain", NoWindow = 7 },
                new Plan { Id = 14, IdDispenser = 108, DateAndTime = DateTime.Parse("2020-03-20 05:00:01"), Description = "lol", NoWindow = 4 },
                new Plan { Id = 15, IdDispenser = 108, DateAndTime = DateTime.Parse("2020-03-22 16:00:01"), Description = "lol", NoWindow = 7 },
                new Plan { Id = 16, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-20 16:00:01"), Description = "Eutanazol", NoWindow = 1 },
                new Plan { Id = 17, IdDispenser = 109, DateAndTime = DateTime.Parse("2020-03-20 17:30:01"), Description = "Eutanazol", NoWindow = 6 },
                new Plan { Id = 18, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-20 15:30:01"), Description = "Apap", NoWindow = 2 },
                new Plan { Id = 19, IdDispenser = 102, DateAndTime = DateTime.Parse("2020-03-20 07:30:01"), Description = "Apap", NoWindow = 4 }
            );

            builder.Entity<ListOfDispenser>().ToTable("ListOfDispensers");
            builder.Entity<ListOfDispenser>().HasKey(p => p.Id);
            builder.Entity<ListOfDispenser>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<ListOfDispenser>().HasData
            (
                new ListOfDispenser { Id = 1, IdDispenser = 101, IdAccount = 1, Name = "a" },
                new ListOfDispenser { Id = 2, IdDispenser = 102, IdAccount = 2, Name = "b" },
                new ListOfDispenser { Id = 3, IdDispenser = 103, IdAccount = 3, Name = "c" },
                new ListOfDispenser { Id = 4, IdDispenser = 104, IdAccount = 4, Name = "d" },
                new ListOfDispenser { Id = 5, IdDispenser = 105, IdAccount = 5, Name = "e" },
                new ListOfDispenser { Id = 6, IdDispenser = 106, IdAccount = 6, Name = "f" },
                new ListOfDispenser { Id = 7, IdDispenser = 107, IdAccount = 7, Name = "g" },
                new ListOfDispenser { Id = 8, IdDispenser = 108, IdAccount = 8, Name = "h" },
                new ListOfDispenser { Id = 9, IdDispenser = 109, IdAccount = 9, Name = "i" },
                new ListOfDispenser { Id = 10, IdDispenser = 101, IdAccount = 6, Name = "j" },
                new ListOfDispenser { Id = 11, IdDispenser = 102, IdAccount = 6, Name = "k" },
                new ListOfDispenser { Id = 12, IdDispenser = 104, IdAccount = 6, Name = "l" },
                new ListOfDispenser { Id = 13, IdDispenser = 103, IdAccount = 7, Name = "m" },
                new ListOfDispenser { Id = 14, IdDispenser = 109, IdAccount = 7, Name = "n" },
                new ListOfDispenser { Id = 15, IdDispenser = 102, IdAccount = 8, Name = "o" },
                new ListOfDispenser { Id = 16, IdDispenser = 109, IdAccount = 8, Name = "p" }
            );

            builder.Entity<Presentation>().ToTable("PresentationTable");
            builder.Entity<Presentation>().HasKey(p => p.Id);
            builder.Entity<Presentation>().Property(p => p.Id).IsRequired().ValueGeneratedOnAdd();

            builder.Entity<Presentation>().HasData
            (
                new Presentation { Id = 1, Flag = 0},
                new Presentation { Id = 2, Flag = 0},
                new Presentation { Id = 3, Flag = 1},
                new Presentation { Id = 4, Flag = -1},
                new Presentation { Id = 5, Flag = -1},
                new Presentation { Id = 6, Flag = 1},
                new Presentation { Id = 7, Flag = 1},
                new Presentation { Id = 8, Flag = 1},
                new Presentation { Id = 9, Flag = 0},
                new Presentation { Id = 10, Flag = -1},
                new Presentation { Id = 11, Flag = 0},
                new Presentation { Id = 12, Flag = 0}
            );
        }
    }
}