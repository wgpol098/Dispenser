using System;
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
                name: "History",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DispenserId = table.Column<int>(nullable: false),
                    Datetime = table.Column<DateTime>(nullable: false),
                    Nr_Okienka = table.Column<int>(nullable: false),
                    Opis = table.Column<string>(nullable: true),
                    Flaga = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_History", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Plans",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DispenserId = table.Column<int>(nullable: false),
                    dateTime = table.Column<DateTime>(nullable: false),
                    Nr_Okienka = table.Column<int>(nullable: false),
                    Opis = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Plans", x => x.Id);
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
                table: "History",
                columns: new[] { "Id", "Datetime", "DispenserId", "Flaga", "Nr_Okienka", "Opis" },
                values: new object[,]
                {
                    { 1, new DateTime(2020, 3, 7, 0, 0, 0, 0, DateTimeKind.Unspecified), 101, 0, 1, "Stimea" },
                    { 2, new DateTime(2020, 3, 7, 13, 0, 0, 0, DateTimeKind.Unspecified), 102, 1, 2, "Iladian" },
                    { 3, new DateTime(2020, 3, 8, 4, 0, 0, 0, DateTimeKind.Unspecified), 101, 0, 3, "Maxigra" },
                    { 4, new DateTime(2020, 3, 8, 8, 0, 0, 0, DateTimeKind.Unspecified), 103, 1, 4, "Limetic" }
                });

            migrationBuilder.InsertData(
                table: "Plans",
                columns: new[] { "Id", "DispenserId", "Nr_Okienka", "Opis", "dateTime" },
                values: new object[,]
                {
                    { 1, 101, 1, "Stimea", new DateTime(2020, 3, 9, 1, 0, 0, 0, DateTimeKind.Unspecified) },
                    { 2, 102, 2, "Iladian", new DateTime(2020, 3, 9, 14, 0, 0, 0, DateTimeKind.Unspecified) },
                    { 3, 102, 3, "Maxigra", new DateTime(2020, 3, 9, 3, 0, 0, 0, DateTimeKind.Unspecified) },
                    { 4, 102, 4, "Limetic", new DateTime(2020, 3, 9, 4, 0, 0, 0, DateTimeKind.Unspecified) }
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
                name: "History");

            migrationBuilder.DropTable(
                name: "Plans");

            migrationBuilder.DropTable(
                name: "Dispensers");
        }
    }
}
