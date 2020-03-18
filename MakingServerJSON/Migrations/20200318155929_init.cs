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
                    { 1, 101, "AndrzejKrawczyk", "Tak123" },
                    { 2, 102, "MateuszWałek", "Nie321" },
                    { 3, 103, "TomekKowalski", "Haslo" },
                    { 4, 104, "PawełNowak", "Test" },
                    { 5, 105, "JakubLewandowski", "Somsiad" },
                    { 6, 106, "MarekPracowity", "Viagra" },
                    { 7, 107, "FranekGolas", "Wojna" },
                    { 8, 108, "AdamMickiewicz", "Pisarz" },
                    { 9, 109, "JanKazimierzKretacz", "Test109" }
                });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "DispenserId", "Nr_Okienka" },
                values: new object[,]
                {
                    { 9, 109, "1000010" },
                    { 8, 108, "0001001" },
                    { 7, 107, "1000001" },
                    { 6, 106, "0001000" },
                    { 5, 105, "0000001" },
                    { 4, 104, "1000000" },
                    { 3, 103, "0100010" },
                    { 2, 102, "1010100" },
                    { 1, 101, "1110000" }
                });

            migrationBuilder.InsertData(
                table: "History",
                columns: new[] { "Id", "DateAndTime", "DispenserId", "Flaga", "Nr_Okienka", "Opis" },
                values: new object[,]
                {
                    { 19, new DateTime(2020, 3, 7, 21, 0, 0, 0, DateTimeKind.Unspecified), 109, 1, 2, "Iladian" },
                    { 18, new DateTime(2020, 3, 7, 10, 0, 0, 0, DateTimeKind.Unspecified), 109, -1, 1, "Maxigra" },
                    { 17, new DateTime(2020, 3, 6, 21, 0, 0, 0, DateTimeKind.Unspecified), 109, 1, 7, "Stimea" },
                    { 16, new DateTime(2020, 3, 6, 8, 0, 0, 0, DateTimeKind.Unspecified), 108, 1, 6, "Limetic" },
                    { 15, new DateTime(2020, 3, 5, 17, 0, 0, 0, DateTimeKind.Unspecified), 107, 1, 5, "Maxigra" },
                    { 14, new DateTime(2020, 3, 5, 8, 0, 0, 0, DateTimeKind.Unspecified), 106, -1, 4, "Iladian" },
                    { 13, new DateTime(2020, 3, 5, 7, 0, 0, 0, DateTimeKind.Unspecified), 105, 1, 3, "Stimea" },
                    { 12, new DateTime(2020, 3, 4, 18, 0, 0, 0, DateTimeKind.Unspecified), 104, 0, 3, "Limetic" },
                    { 11, new DateTime(2020, 3, 4, 14, 0, 0, 0, DateTimeKind.Unspecified), 103, 1, 2, "Maxigra" },
                    { 9, new DateTime(2020, 3, 3, 17, 0, 0, 0, DateTimeKind.Unspecified), 101, 1, 1, "Stimea" },
                    { 1, new DateTime(2020, 3, 1, 9, 0, 0, 0, DateTimeKind.Unspecified), 109, 0, 1, "Stimea" },
                    { 8, new DateTime(2020, 3, 3, 13, 0, 0, 0, DateTimeKind.Unspecified), 102, 1, 1, "Limetic" },
                    { 7, new DateTime(2020, 3, 3, 8, 0, 0, 0, DateTimeKind.Unspecified), 103, 1, 7, "Maxigra" },
                    { 6, new DateTime(2020, 3, 2, 18, 0, 0, 0, DateTimeKind.Unspecified), 104, -1, 6, "Iladian" },
                    { 5, new DateTime(2020, 3, 2, 9, 0, 0, 0, DateTimeKind.Unspecified), 105, 1, 5, "Stimea" },
                    { 4, new DateTime(2020, 3, 2, 6, 0, 0, 0, DateTimeKind.Unspecified), 106, 1, 4, "Limetic" },
                    { 3, new DateTime(2020, 3, 1, 16, 0, 0, 0, DateTimeKind.Unspecified), 107, 0, 3, "Maxigra" },
                    { 2, new DateTime(2020, 3, 1, 11, 0, 0, 0, DateTimeKind.Unspecified), 108, 1, 2, "Iladian" },
                    { 10, new DateTime(2020, 3, 4, 9, 0, 0, 0, DateTimeKind.Unspecified), 102, -1, 2, "Iladian" }
                });

            migrationBuilder.InsertData(
                table: "Plans",
                columns: new[] { "Id", "DateAndTime", "DispenserId", "Nr_Okienka", "Opis" },
                values: new object[,]
                {
                    { 11, new DateTime(2020, 3, 25, 9, 0, 0, 0, DateTimeKind.Unspecified), 106, 4, "ddd" },
                    { 12, new DateTime(2020, 3, 20, 1, 0, 0, 0, DateTimeKind.Unspecified), 107, 1, "brain" },
                    { 16, new DateTime(2020, 3, 20, 16, 0, 0, 0, DateTimeKind.Unspecified), 109, 1, "Eutanazol" },
                    { 14, new DateTime(2020, 3, 20, 5, 0, 0, 0, DateTimeKind.Unspecified), 108, 4, "lol" },
                    { 15, new DateTime(2020, 3, 22, 16, 0, 0, 0, DateTimeKind.Unspecified), 108, 7, "lol" },
                    { 10, new DateTime(2020, 3, 20, 4, 0, 0, 0, DateTimeKind.Unspecified), 105, 7, "ccc" },
                    { 13, new DateTime(2020, 3, 20, 2, 0, 0, 0, DateTimeKind.Unspecified), 107, 7, "brain" },
                    { 9, new DateTime(2020, 3, 20, 5, 0, 0, 0, DateTimeKind.Unspecified), 104, 1, "bbb" },
                    { 4, new DateTime(2020, 3, 20, 6, 0, 0, 0, DateTimeKind.Unspecified), 102, 1, "IBUM" },
                    { 7, new DateTime(2020, 3, 20, 4, 0, 0, 0, DateTimeKind.Unspecified), 103, 2, "aaa" },
                    { 6, new DateTime(2020, 3, 21, 6, 0, 0, 0, DateTimeKind.Unspecified), 102, 5, "IBUM" },
                    { 5, new DateTime(2020, 3, 20, 18, 0, 0, 0, DateTimeKind.Unspecified), 102, 3, "IBUM" },
                    { 3, new DateTime(2020, 3, 22, 13, 0, 0, 0, DateTimeKind.Unspecified), 101, 3, "Apap" },
                    { 2, new DateTime(2020, 3, 21, 13, 0, 0, 0, DateTimeKind.Unspecified), 101, 2, "Apap" },
                    { 1, new DateTime(2020, 3, 20, 13, 0, 0, 0, DateTimeKind.Unspecified), 101, 1, "Apap" },
                    { 8, new DateTime(2020, 3, 20, 23, 0, 0, 0, DateTimeKind.Unspecified), 103, 6, "aaa" },
                    { 17, new DateTime(2020, 3, 20, 17, 30, 0, 0, DateTimeKind.Unspecified), 109, 6, "Eutanazol" }
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
