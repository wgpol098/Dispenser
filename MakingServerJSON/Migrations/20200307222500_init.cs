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
                    TurnDiode = table.Column<bool>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Dispensers", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Accounts",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Login = table.Column<string>(nullable: true),
                    Password = table.Column<string>(nullable: true),
                    DispenserId = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Accounts", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Accounts_Dispensers_DispenserId",
                        column: x => x.DispenserId,
                        principalTable: "Dispensers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "Name", "TurnDiode" },
                values: new object[,]
                {
                    { 100, "Cos", false },
                    { 101, "Tam", true },
                    { 102, "Yayy", false }
                });

            migrationBuilder.InsertData(
                table: "Accounts",
                columns: new[] { "Id", "DispenserId", "Login", "Password" },
                values: new object[,]
                {
                    { 2, 100, "Moze", "Byc" },
                    { 3, 100, "Ten", "Trzeci" },
                    { 1, 101, "Andrzej", "Tak1223" },
                    { 4, 101, "Oraz", "Czw" },
                    { 5, 102, "Koko", "Ro" }
                });

            migrationBuilder.CreateIndex(
                name: "IX_Accounts_DispenserId",
                table: "Accounts",
                column: "DispenserId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Accounts");

            migrationBuilder.DropTable(
                name: "Dispensers");
        }
    }
}
