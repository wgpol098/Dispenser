using Microsoft.EntityFrameworkCore;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Persistence.Contexts
{
    public class AppDbContext : DbContext
    {
        public DbSet<Dispenser> Dispensers { get; set; }
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
                new Dispenser { Id = 101, Name = "Tam", TurnDiode = true}
            );
        }
    }
}
