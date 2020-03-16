using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace WebApplication1.Migrations
{
    public partial class init : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
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
                });

            migrationBuilder.CreateTable(
                name: "Dispensers",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DispenserId = table.Column<int>(nullable: false),
                    Nr_Okienka = table.Column<string>(nullable: true)
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
                    DateAndTime = table.Column<DateTime>(nullable: false),
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
                    DateAndTime = table.Column<DateTime>(nullable: false),
                    Nr_Okienka = table.Column<int>(nullable: false),
                    Opis = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Plans", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "Accounts",
                columns: new[] { "Id", "DispenserId", "Login", "Password" },
                values: new object[,]
                {
                    { 1, 101, "Andrzej", "Tak1223" },
                    { 2, 100, "Moze", "Byc" },
                    { 3, 100, "Ten", "Trzeci" },
                    { 4, 101, "Oraz", "Czw" },
                    { 5, 102, "Koko", "Ro" }
                });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "DispenserId", "Nr_Okienka" },
                values: new object[,]
                {
                    { 9, 108, "0101010" },
                    { 7, 106, "0001001" },
                    { 6, 105, "0001000" },
                    { 8, 107, "0001100" },
                    { 4, 103, "1011101" },
                    { 2, 102, "1001001" },
                    { 1, 101, "1010011" },
                    { 5, 104, "1001001" }
                });

            migrationBuilder.InsertData(
                table: "History",
                columns: new[] { "Id", "DateAndTime", "DispenserId", "Flaga", "Nr_Okienka", "Opis" },
                values: new object[,]
                {
                    { 1, new DateTime(2020, 3, 7, 0, 0, 0, 0, DateTimeKind.Unspecified), 101, 0, 1, "Stimea" },
                    { 2, new DateTime(2020, 3, 7, 13, 0, 0, 0, DateTimeKind.Unspecified), 102, 1, 2, "Iladian" },
                    { 3, new DateTime(2020, 3, 8, 4, 0, 0, 0, DateTimeKind.Unspecified), 101, 0, 3, "Maxigra" },
                    { 4, new DateTime(2020, 3, 8, 8, 0, 0, 0, DateTimeKind.Unspecified), 103, 1, 4, "Limetic" }
                });

            migrationBuilder.InsertData(
                table: "Plans",
                columns: new[] { "Id", "DateAndTime", "DispenserId", "Nr_Okienka", "Opis" },
                values: new object[,]
                {
                    { 5, new DateTime(2020, 3, 10, 4, 0, 0, 0, DateTimeKind.Unspecified), 102, 5, "IBUM" },
                    { 1, new DateTime(2020, 3, 9, 1, 0, 0, 0, DateTimeKind.Unspecified), 102, 1, "Apap" },
                    { 2, new DateTime(2020, 3, 10, 14, 0, 0, 0, DateTimeKind.Unspecified), 102, 2, "Apap" },
                    { 3, new DateTime(2020, 3, 11, 3, 0, 0, 0, DateTimeKind.Unspecified), 102, 3, "Apap" },
                    { 4, new DateTime(2020, 3, 9, 4, 0, 0, 0, DateTimeKind.Unspecified), 102, 4, "IBUM" },
                    { 6, new DateTime(2020, 3, 11, 4, 0, 0, 0, DateTimeKind.Unspecified), 102, 6, "IBUM" }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Accounts");

            migrationBuilder.DropTable(
                name: "Dispensers");

            migrationBuilder.DropTable(
                name: "History");

            migrationBuilder.DropTable(
                name: "Plans");
        }
    }
}
