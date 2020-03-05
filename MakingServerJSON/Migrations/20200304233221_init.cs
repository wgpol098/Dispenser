using Microsoft.EntityFrameworkCore.Migrations;

namespace WebApplication1.Migrations
{
    public partial class init : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Dispensers",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(maxLength: 30, nullable: false),
                    Login = table.Column<string>(nullable: true),
                    Password = table.Column<string>(nullable: true),
                    TurnDiode = table.Column<bool>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Dispensers", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "Login", "Name", "Password", "TurnDiode" },
                values: new object[] { 100, "Andrzej", "Cos", "Tak1223", false });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "Login", "Name", "Password", "TurnDiode" },
                values: new object[] { 101, "Moze", "Tam", "Byc", true });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Dispensers");
        }
    }
}
